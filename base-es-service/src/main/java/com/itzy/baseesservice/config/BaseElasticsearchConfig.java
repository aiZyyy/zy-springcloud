package com.itzy.baseesservice.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ZY
 * @date 2020/8/18 14:33
 * @Description: es配置文件
 * <p>
 * SpringBoot 集成 ES 的步骤：
 * 1、导入依赖
 * https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-high-getting-started-maven.html
 * 2、编写 ES 配置，给容器中注入一个 RestHighLevelClient，用来操作 9200 端口
 * https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-high-getting-started-initialization.html
 * 3、参照官方API
 * @Version 1.0
 */
@Configuration
@EnableConfigurationProperties
public class BaseElasticsearchConfig {
    @Bean
    public RestHighLevelClient esRestHighLevelClient() {
        RestHighLevelClient client = new RestHighLevelClient(
                // 这里可以配置多个 es服务，我当前服务不是集群，所以目前只配置一个
                RestClient.builder(new HttpHost("122.51.211.252", 9200, "http")));
        return client;
    }
}
