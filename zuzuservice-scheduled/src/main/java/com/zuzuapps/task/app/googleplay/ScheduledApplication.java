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
import com.zuzuapps.task.app.common.CommonUtils;
import com.zuzuapps.task.app.googleplay.models.SummaryApplicationPlays;
import com.zuzuapps.task.app.googleplay.servies.SummaryApplicationPlayService;
import com.zuzuapps.task.app.master.models.CountryMaster;
import com.zuzuapps.task.app.master.repositories.CountryMasterRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

@SpringBootApplication
@Import({GooglePlayCommonConfiguration.class})
public class ScheduledApplication {
    private final ObjectMapper mapper = new ObjectMapper();
    private final Log logger = LogFactory.getLog(ScheduledApplication.class);

    @Value("${data.root.path:/tmp}")
    private String rootPath;
    @Autowired
    private SummaryApplicationPlayService summaryApplicationPlayService;
    @Autowired
    private CountryMasterRepository countryRepository;

    public static void main(String[] args) {
        SpringApplication.run(ScheduledApplication.class, args);
    }

    public void delay(long time) {
        try {
            logger.debug("Stop in " + time / 1000 + "s");
            Thread.sleep(time);
        } catch (Exception e) {
        }
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void appSummary() {
        logger.info("[Application Summary]Cronjob start at: " + new Date());
        // something that should execute on weekdays only
        List<CountryMaster> countries = countryRepository.findAllByTypeGreaterThanOrderByTypeDesc(0);
        for (CountryMaster countryMaster : countries) {
            for (CollectionEnum collection : CollectionEnum.values()) {
                for (CategoryEnum category : CategoryEnum.values()) {
                    try {
                        SummaryApplicationPlays summaryApplicationPlays
                                = summaryApplicationPlayService.getSummaryApplications(category, collection, countryMaster.getLanguageCode(), countryMaster.getCountryCode(), 0);
                        String path = CommonUtils.getFolderBy(rootPath, countryMaster.getCountryCode(), category.name(), collection.name(),false);
                        Files.write(Paths.get(path + "/" + System.currentTimeMillis() + ".json"), mapper.writeValueAsBytes(summaryApplicationPlays));
                    } catch (IOException ex) {

                    } catch (Exception ex) {
                        logger.error(ex);
                    }
                    delay(5000);
                }
            }
        }
        logger.info("[Application Summary]Cronjob end at: " + new Date());
    }

    @Scheduled(cron = "0/5 * * * * *")
    public void applicationInformation() {
        // logger.info("[Application Information]Cronjob start at: " + new Date());
        // logger.info("[Application Information]Cronjob end at: " + new Date());
    }

    @Scheduled(cron = "0/10 * * * * *")
    public void screenshootExtract() {
        // logger.info("[Screenshoot Extract]Cronjob start at: " + new Date());
        // logger.info("[Screenshoot Extract]Cronjob end at: " + new Date());
    }
}