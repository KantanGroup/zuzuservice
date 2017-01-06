/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zuzuapps.task.app.googleplay;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zuzuapps.task.app.GooglePlayCommonConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.Message;

@SpringBootApplication
@EnableBinding(Sink.class)
@EnableConfigurationProperties(AppLogSinkProperties.class)
@Import(GooglePlayCommonConfiguration.class)
public class AppLogSinkApplication {

    @Autowired
    private AppLogSinkProperties properties;

    @StreamListener(Sink.INPUT)
    public void handlerMessage(Message<Object> data) throws Exception {
        final ObjectMapper mapper = new ObjectMapper();
        System.out.println(mapper.writeValueAsString(data.getPayload()));
    }

    public static void main(String[] args) {
        SpringApplication.run(AppLogSinkApplication.class, args);
    }

}