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
import com.zuzuapps.task.app.common.CategoryEnum;
import com.zuzuapps.task.app.common.CollectionEnum;
import com.zuzuapps.task.app.googleplay.models.SummaryApplicationPlays;
import com.zuzuapps.task.app.googleplay.servies.SummaryApplicationPlayService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;

@SpringBootApplication
@Import({GooglePlayCommonConfiguration.class})
public class ScheduledApplication {
    private final ObjectMapper mapper = new ObjectMapper();
    private final Log logger = LogFactory.getLog(ScheduledApplication.class);

    @Autowired
    private SummaryApplicationPlayService summaryApplicationPlayService;

    public static void main(String[] args) {
        SpringApplication.run(ScheduledApplication.class, args);
    }

    public void delay(long time) {
        try {
            Thread.sleep(time);
        } catch (Exception e) {
        }
    }

    @Scheduled(cron = "0/5 * * * * *")
    public void appSummary() {
        // something that should execute on weekdays only
        /*
        for (CollectionEnum collection : CollectionEnum.values()) {
            for (CategoryEnum category : CategoryEnum.values()) {
                try {
                    SummaryApplicationPlays summaryApplicationPlays
                            = summaryApplicationPlayService.getSummaryApplications(category, collection, properties.getLanguageCode(), properties.getCountryCode(), 1);
                    logger.info(mapper.writeValueAsString(summaryApplicationPlays));
                } catch (Exception ex) {
                    logger.error(ex);
                    deday(30000);
                }
                delay(5000);
            }
        }
        //*/
        logger.info("[Application Summary]Cronjob run at: " + new Date());
    }

    @Scheduled(cron = "*/10 * * * * *")
    public void screenshootExtract() {
        // something that should execute on weekdays only
        logger.info("[Screenshoot Extract]Cronjob run at: " + new Date());
    }
}