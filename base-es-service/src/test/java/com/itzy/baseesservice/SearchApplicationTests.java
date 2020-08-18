package com.itzy.baseesservice;

import org.elasticsearch.client.RestHighLevelClient;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author ZY
 * @date 2020/8/18 15:03
 * @Description:
 * @Version 1.0
 */
@SpringBootTest
public class SearchApplicationTests {
    @Autowired
    private RestHighLevelClient client;

    @Test
    public void contextLoads() {
        System.out.println(client);
    }
}
