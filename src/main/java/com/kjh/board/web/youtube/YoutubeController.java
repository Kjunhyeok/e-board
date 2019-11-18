package com.kjh.board.web.youtube;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Type;
import java.util.List;

@RestController
@Slf4j
public class YoutubeController {

    private Type listType = new TypeToken<List<SearchVO>>() {}.getType();
    private Gson gson = new Gson();

    @RequestMapping(value = "/search/{id}", method=RequestMethod.GET)
    public String search(@PathVariable String id){
        String json = gson.toJson(Search.get(id), listType);
        log.info("json : {}", json);
        return json;
    }
}
