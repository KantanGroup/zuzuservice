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

import com.zuzuapps.task.app.GooglePlayCommonConfiguration;
import com.zuzuapps.task.app.common.CategoryEnum;
import com.zuzuapps.task.app.common.CollectionEnum;
import com.zuzuapps.task.app.googleplay.models.SummaryApplicationPlays;
import com.zuzuapps.task.app.googleplay.servies.SummaryApplicationPlayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.app.trigger.TriggerConfiguration;
import org.springframework.cloud.stream.app.trigger.TriggerProperties;
import org.springframework.cloud.stream.app.trigger.TriggerPropertiesMaxMessagesDefaultOne;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Import;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.messaging.support.MessageBuilder;

import java.util.Date;

@SpringBootApplication
@EnableBinding(Source.class)
@Import({TriggerConfiguration.class, GooglePlayCommonConfiguration.class})
@EnableConfigurationProperties({AppSummarySourceProperties.class, TriggerPropertiesMaxMessagesDefaultOne.class})
public class AppSummarySourceApplication {
    @Autowired
    private Source source;

    @Autowired
    private SummaryApplicationPlayService summaryApplicationPlayService;

    @Autowired
    private AppSummarySourceProperties properties;

    @Autowired
    private TriggerProperties triggerProperties;

    public static void main(String[] args) {
        SpringApplication.run(AppSummarySourceApplication.class, args);
    }

    @InboundChannelAdapter(value = Source.OUTPUT, poller = @Poller("defaultPoller"))
    public String pollAppSummaryToSource() {
        for (CollectionEnum collection : CollectionEnum.values()) {
            for (CategoryEnum category : CategoryEnum.values()) {
                try {
                    SummaryApplicationPlays summaryApplicationPlays = summaryApplicationPlayService.getSummaryApplications(category, collection, properties.getLanguageCode(), properties.getCountryCode(), 1);
                    source.output().send(MessageBuilder.withPayload(summaryApplicationPlays).build());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                delay();
            }
        }
        return new String("[App][Country:" + properties.getCountryCode().toUpperCase() + "][Language:" + properties.getLanguageCode().toUpperCase() + "] Start at: " + new Date());
    }

    public void delay() {
        try {
            Thread.sleep(properties.getDelayTime() * 1000);
        } catch (Exception e) {
        }
    }

}