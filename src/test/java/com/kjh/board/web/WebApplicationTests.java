package com.kjh.board.web;

import com.kjh.board.web.youtube.SearchVO;
import com.kjh.board.web.youtube.Search;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WebApplicationTests {

    @Test
    public void contextLoads() {
        List<SearchVO> list = Search.get("708vnNjnbAA");
    }

}
