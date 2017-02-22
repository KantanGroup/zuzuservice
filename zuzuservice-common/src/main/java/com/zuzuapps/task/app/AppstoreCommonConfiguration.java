package com.zuzuapps.task.app;

import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;
import org.springframework.data.solr.server.support.MulticoreSolrClientFactory;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @author tuanta17
 */
@SpringBootApplication // same as @Configuration @EnableAutoConfiguration @ComponentScan
@EnableElasticsearchRepositories(basePackages = "com.zuzuapps.task.app.elasticsearch.repositories")
@EnableSolrRepositories(basePackages = {"com.zuzuapps.task.app.solr.repositories"}, multicoreSupport = true)
@EnableJpaRepositories
public class AppstoreCommonConfiguration {

    @Bean
    public RestTemplate restTemplate(List<HttpMessageConverter<?>> messageConverters) {
        return new RestTemplate(messageConverters);
    }

    @Bean
    public ByteArrayHttpMessageConverter byteArrayHttpMessageConverter() {
        return new ByteArrayHttpMessageConverter();
    }

    @Bean
    public MulticoreSolrClientFactory solrClientFactory() {
        return new MulticoreSolrClientFactory(new HttpSolrClient("http://localhost:8983/solr"));
    }
}
