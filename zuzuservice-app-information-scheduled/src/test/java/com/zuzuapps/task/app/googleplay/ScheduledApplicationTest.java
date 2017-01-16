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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({GooglePlayCommonConfiguration.class})
public class ScheduledApplicationTest {
    public static final String COUNTRY_CODE_DEFAULT = "us";
    public static final String LANGUAGE_CODE_DEFAULT = "en";
    private final ObjectMapper mapper = new ObjectMapper();
    private final Log logger = LogFactory.getLog(ScheduledApplicationTest.class);

    @Value("${data.root.path:/tmp}")
    private String rootPath;
    @Autowired
    private SummaryApplicationPlayService summaryApplicationPlayService;
    @Autowired
    private CountryMasterRepository countryRepository;

    public void delay(long time) {
        try {
            logger.debug("Stop in " + time / 1000 + "s");
            Thread.sleep(time);
        } catch (Exception e) {
        }
    }

    @Test
    public void appTop() {
        logger.info("[Application Top]Cronjob start at: " + new Date());
        // something that should execute on weekdays only
        String time = CommonUtils.getDailyByTime();
        List<CountryMaster> countries = countryRepository.findAllByTypeGreaterThanOrderByTypeDesc(0);
        for (CountryMaster countryMaster : countries) {
            for (CollectionEnum collection : CollectionEnum.values()) {
                for (CategoryEnum category : CategoryEnum.values()) {
                    try {
                        SummaryApplicationPlays summaryApplicationPlays
                                = summaryApplicationPlayService.getSummaryApplications(category, collection, countryMaster.getLanguageCode(), countryMaster.getCountryCode(), 0);
                        StringBuilder path = new StringBuilder(CommonUtils.queueTopFolderBy(rootPath, time));
                        path.append("/").append(countryMaster.getCountryCode()).append("___");
                        path.append(category.name().toLowerCase()).append("___");
                        path.append(collection.name().toLowerCase()).append("___").append(time + ".json");
                        Files.write(Paths.get(path.toString()), mapper.writeValueAsBytes(summaryApplicationPlays));
                    } catch (Exception ex) {
                        logger.error(ex);
                    }
                    delay(5000);
                }
            }
        }
        logger.info("[Application Top]Cronjob end at: " + new Date());
    }

    @Test
    public void appSummary() {
        logger.info("[Application Summary]Cronjob start at: " + new Date());
        // something that should execute on weekdays only
        String time = CommonUtils.getDailyByTime();
        for (CollectionEnum collection : CollectionEnum.values()) {
            for (CategoryEnum category : CategoryEnum.values()) {
                int page = 1;
                while (true) {
                    try {
                        SummaryApplicationPlays summaryApplicationPlays
                                = summaryApplicationPlayService.getSummaryApplications(category, collection, LANGUAGE_CODE_DEFAULT, COUNTRY_CODE_DEFAULT, page);
                        StringBuilder path = new StringBuilder(CommonUtils.queueSummaryFolderBy(rootPath, time));
                        path.append("/").append(COUNTRY_CODE_DEFAULT).append("___");
                        path.append(category.name().toLowerCase()).append("___");
                        path.append(collection.name().toLowerCase()).append("___");
                        path.append(time).append("___").append(page).append(".json");
                        Files.write(Paths.get(path.toString()), mapper.writeValueAsBytes(summaryApplicationPlays));
                        if (summaryApplicationPlays.getResults().size() < 120) {
                            break;
                        }
                    } catch (Exception ex) {
                        logger.error(ex);
                        break;
                    }
                    page++;
                    delay(5000);
                }
            }
        }
        logger.info("[Application Summary]Cronjob end at: " + new Date());
    }

    @Test
    public void update() {
        logger.info("[Application Information]Cronjob start at: " + new Date());
        ObjectMapper mapper = new ObjectMapper();
        // something that should execute on weekdays only
        String time = CommonUtils.getMinutelyByTime();
        String dirPath = CommonUtils.queueTopFolderBy(rootPath, time);
        File dir = new File(dirPath);
        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            File json = files[0];
            String filename = json.getName();
            System.out.println(filename);
            /*
            try {
                SummaryApplicationPlays apps = mapper.readValue(json, SummaryApplicationPlays.class);
                // Add data to mysql
                int i = 0;
                for(SummaryApplicationPlay app: apps.getResults()) {
                    AppIndexMaster appIndexMaster = new AppIndexMaster();
                    appIndexMaster.setAppId(app.getAppId());
                    appIndexMaster.setCategory();

                }
                apps.getResults().size();
                // Add data to elasticsearch

            } catch (Exception e) {
                logger.error("Convert data from json error " + json.getAbsolutePath());
            }
            */
            delay(100);
        }
        logger.info("[Application Information]Cronjob end at: " + new Date());
    }
}