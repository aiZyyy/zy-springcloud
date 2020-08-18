package com.itzy.baseesservice;

import com.alibaba.fastjson.JSON;
import com.itzy.baseesservice.config.BaseElasticsearchConfig;
import lombok.Data;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

/**
 * @author ZY
 * @date 2020/8/18 15:03
 * @Description:
 * @Version 1.0
 */
@SpringBootTest
@EnableAutoConfiguration
public class SearchApplicationTests {
    @Autowired
    private RestHighLevelClient client;

    @Test
    public void contextLoads() {
        System.out.println(client);
    }

    /**
     * 测试存储数据到es
     * 更新也是可以的
     */
    @Test
    public void indexData() throws IOException {
        // 准备数据
        IndexRequest indexRequest = new IndexRequest("users");//索引名
        indexRequest.id("1");//数据id

        // 第一种方式
//        indexRequest.source("userName", "zhangsan", "age", 18, "gender", "男");

        // 第二种方式（推荐使用）
        User user = new User();
        user.setUserName("zhangsan");
        user.setAge(18);
        user.setGender("男");

        String jsonString = JSON.toJSONString(user);
        indexRequest.source(jsonString, XContentType.JSON); //要保存的内容

        // 执行操作
        IndexResponse index = client.index(indexRequest, BaseElasticsearchConfig.COMMON_OPTIONS);

        // 提取有用的数据相应
        System.out.println(index);
    }

    @Data
    class User{
        private String userName;
        private Integer age;
        private String gender;
    }

}
