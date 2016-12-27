package org.springframework.cloud.task.app;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * @author tuanta17
 */
@SpringBootApplication // same as @Configuration @EnableAutoConfiguration @ComponentScan
public class GooglePlayCommonConfiguration {

    @Bean
    public RestTemplate createRestTemplate() {
        return new RestTemplate();
    }
}
