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
import com.zuzuapps.task.app.common.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.File;

@SpringBootApplication
@Import({GooglePlayCommonConfiguration.class})
public class ScheduleApplication {

    @Value("${data.root.path:/tmp}")
    private String rootPath;
    @Autowired
    private AppIndexService appIndexService;
    @Autowired
    private AppSummaryService appSummaryService;

    public static void main(String[] args) {
        SpringApplication.run(ScheduleApplication.class, args);
    }

    @Bean
    public DailyAppIndexTask scheduleDailyAppIndexUpdate() {
        return new DailyAppIndexTask();
    }

    /**
     * A commandline runner that prints a timestamp.
     */
    public class DailyAppIndexTask implements CommandLineRunner {
        @Override
        public void run(String... strings) throws Exception {
            while(true) {
                // something that should execute on weekdays only
                String time = CommonUtils.getDailyByTime();
                String dirPath = CommonUtils.queueTopFolderBy(rootPath, time);
                File dir = new File(dirPath);
                File[] files = dir.listFiles();
                if (files != null && files.length != 0) {
                    appIndexService.dailyAppIndexUpdate(files);
                }
                CommonUtils.delay(5000);
            }
        }
    }

    /**
     * Write top app info of category in to json
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void scheduleAppTop() {
        appIndexService.appIndex();
    }

    /**
     * Write app summary in USA to json
     */
    @Scheduled(cron = "2 0 0 1,11,21 * *")
    public void scheduleAppSummary() {
        appSummaryService.appSummary();
    }
}