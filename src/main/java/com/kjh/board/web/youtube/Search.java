package com.kjh.board.web.youtube;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

@Slf4j
public class Search {
    /** Global instance properties filename. */
    private static String PROPERTIES_FILENAME = "youtube.properties";

    /** Global instance of the HTTP transport. */
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();

    /** Global instance of the max number of videos we want returned (50 = upper limit per page). */
    private static final long NUMBER_OF_VIDEOS_RETURNED = 1;

    /** Global instance of Youtube object to make all API requests. */
    private static YouTube youtube;

    public static List<SearchVO> get(String id) {
        Properties properties = new Properties();
        try {
            InputStream in = Search.class.getResourceAsStream("/" + PROPERTIES_FILENAME);
            properties.load(in);

            /*
             * The YouTube object is used to make all API requests. The last argument is required, but
             * because we don't need anything initialized when the HttpRequest is initialized, we override
             * the interface and provide a no-op function.
             */
            youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, request -> {

            }).setApplicationName("youtube-cmdline-search-sample").build();

            YouTube.Search.List search = youtube.search().list("id,snippet");
            /*
             * It is important to set your API key from the Google Developer Console for
             * non-authenticated requests (found under the Credentials tab at this link:
             * console.developers.google.com/). This is good practice and increased your quota.
             */
            String apiKey = properties.getProperty("youtube.apikey");
            search.setKey(apiKey);
            search.setQ(id);
            /*
             * We are only searching for videos (not playlists or channels). If we were searching for
             * more, we would add them as a string like this: "video,playlist,channel".
             */
            search.setType("video");
            /*
             * This method reduces the info returned to only the fields we need and makes calls more
             * efficient.
             */
            search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
            search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);
            SearchListResponse searchResponse = search.execute();

            List<SearchResult> searchResultList = searchResponse.getItems();

            if (searchResultList != null) {
                return prettyGet(searchResultList.iterator(), id);
            }
        } catch (GoogleJsonResponseException e) {
            log.error("There was a service error: {}", e.getDetails().getCode());
        } catch (IOException e) {
            log.error("here was an IO error:", e);
        } catch (Throwable t) {
            log.error("Throwable", t);
        }
        return null;
    }

    private static List<SearchVO> prettyGet(Iterator<SearchResult> iteratorSearchResults, String query) {
        List<SearchVO> list = new ArrayList<>();
        log.info("Query : {}", query);
        if (!iteratorSearchResults.hasNext()) {
            log.warn("There aren't any results for your query.");
        } else {
            while (iteratorSearchResults.hasNext()) {
                SearchResult singleVideo = iteratorSearchResults.next();
                ResourceId rId = singleVideo.getId();
                SearchVO searchVO = new SearchVO();

                // Double checks the kind is video.
                if (rId.getKind().equals("youtube#video")) {
                    String id = "https://www.youtube.com/embed/" + rId.getVideoId();
                    log.info("Video Id {}", id);

                    Thumbnail thumbnail = singleVideo.getSnippet().getThumbnails().getDefault();
                    log.info("Thumbnail Url : {}", thumbnail.getUrl());

                    String title = singleVideo.getSnippet().getTitle();
                    log.info("Title: {}", title);

                    searchVO.setUrl(id);
                    searchVO.setThumbnail(thumbnail.getUrl());
                    searchVO.setTitle(title);
                    list.add(searchVO);
                }
            }
        }
        return list;
    }
}
