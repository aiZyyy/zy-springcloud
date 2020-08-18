package com.itzy.baseesservice.config;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${elasticsearch.hostname}")
    private String hostname;

    @Value("${elasticsearch.port}")
    private int port;

    /**
     * LowLevelRestConfig
     *
     * @param
     * @date 2019/8/12 18:56
     */
    @Bean
    public RestClient restClient() {
        // 如果有多个从节点可以持续在内部new多个HttpHost，参数1是IP，参数2是端口，参数3是通信协议
        RestClientBuilder clientBuilder = RestClient.builder(new HttpHost(hostname, port, "http"));
        // 设置Header编码
        Header[] defaultHeaders = {new BasicHeader("content-type", "application/json")};
        clientBuilder.setDefaultHeaders(defaultHeaders);
        // 添加其他配置，这些配置都是可选的，详情配置可看https://blog.csdn.net/jacksonary/article/details/82729556
        return clientBuilder.build();
    }

    /**
     * HighLevelRestConfig
     *
     * @param
     * @date 2019/8/12 18:56
     */
    @Bean
    public RestHighLevelClient restHighLevelClient() {
        // 如果有多个从节点可以持续在内部new多个HttpHost，参数1是IP，参数2是端口，参数3是通信协议
        return new RestHighLevelClient(RestClient.builder(new HttpHost(hostname, port, "http")));
    }
}
