package com.zuzuapps.task.app;

import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;
import org.springframework.data.solr.server.SolrClientFactory;
import org.springframework.data.solr.server.support.HttpSolrClientFactory;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @author tuanta17
 */
@SpringBootApplication // same as @Configuration @EnableAutoConfiguration @ComponentScan
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

    // Solr cloud
    /*
    @Value("${spring.data.solr.zk-host}")
    private String solrHost;

    public SolrClientFactory appInformationClientFactory() {
        return new HttpSolrClientFactory(new CloudSolrClient(solrHost), "app-information-index");
    }

    @Bean
    public SolrTemplate appInformationTemplate() throws Exception {
        return new SolrTemplate(appInformationClientFactory());
    }
    //*/

    // Solr stand alone
    //*
    @Value("${spring.data.solr.host}")
    private String solrHost;

    public SolrClientFactory appInformationClientFactory() {
        return new HttpSolrClientFactory(new HttpSolrClient(solrHost), "app-information-index");
    }

    @Bean
    public SolrTemplate appInformationTemplate() throws Exception {
        return new SolrTemplate(appInformationClientFactory());
    }
    //*/
}
