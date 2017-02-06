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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;

@SpringBootApplication
@Import({GooglePlayCommonConfiguration.class})
public class ScheduleApplication {
    final Log logger = LogFactory.getLog("ScheduleApplication");

    @Value("${data.root.path:/tmp}")
    private String rootPath;
    @Autowired
    private AppCommonService appCommonService;
    @Autowired
    private AppIndexService appIndexService;
    @Autowired
    private AppSummaryService appSummaryService;
    @Autowired
    private AppInformationService appLanguageService;

    public static void main(String[] args) {
        SpringApplication.run(ScheduleApplication.class, args);
    }

    @Bean
    public CommandLineRunner schedulingRunner(final TaskExecutor executor) {
        appCommonService.importCountries();
        return new CommandLineRunner() {
            public void run(String... args) throws Exception {
                executor.execute(new DailyIndexUpdateRunnable());
                executor.execute(new DailySummaryUpdateRunnable());
                executor.execute(new DailyAppUpdateRunnable());
                executor.execute(new DailyAppScreenshotRunnable());
                executor.execute(new GenerationIndexRunnable());
                executor.execute(new GenerationSummaryRunnable());
                executor.execute(new IndexStoreRunnable());
                executor.execute(new SummaryStoreRunnable());
            }
        };
    }

    /**
     * Write top app info of category in to json
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void scheduleAppTop() {
        appIndexService.generateAppIndexStoreData();
    }

    /**
     * Write app summary in USA to json
     */
    @Scheduled(cron = "2 0 0 1,11,21 * *")
    public void scheduleAppSummary() {
        appSummaryService.generateAppSummaryStore();
    }

    class DailyIndexUpdateRunnable implements Runnable {

        @Override
        public void run() {
            logger.info("[ScheduleApplication][DailyIndexUpdateRunnable]Start at " + new Date());
            appIndexService.dailyAppIndexUpdate();
        }
    }

    class DailySummaryUpdateRunnable implements Runnable {

        @Override
        public void run() {
            logger.info("[ScheduleApplication][DailySummaryUpdateRunnable]Start at " + new Date());
            appSummaryService.dailyAppSummaryUpdate();
        }
    }

    class DailyAppUpdateRunnable implements Runnable {

        @Override
        public void run() {
            logger.info("[ScheduleApplication][DailyAppUpdateRunnable]Start at " + new Date());
            appLanguageService.dailyAppInformationUpdate();
        }
    }

    class DailyAppScreenshotRunnable implements Runnable {
        @Override
        public void run() {
            logger.info("[ScheduleApplication][DailyAppScreenshotRunnable]Start at " + new Date());
            appLanguageService.dailyAppScreenshotUpdate();
        }
    }

    class GenerationIndexRunnable implements Runnable {

        @Override
        public void run() {
            logger.info("[ScheduleApplication][GenerationIndexRunnable]Start at " + new Date());
            appIndexService.generateAppIndexStoreData();
        }
    }

    class GenerationSummaryRunnable implements Runnable {

        @Override
        public void run() {
            logger.info("[ScheduleApplication][GenerationSummaryRunnable]Start at " + new Date());
            appSummaryService.generateAppSummaryStore();
        }
    }

    class IndexStoreRunnable implements Runnable {

        @Override
        public void run() {
            logger.info("[ScheduleApplication][IndexStoreRunnable]Start at " + new Date());
            appIndexService.appIndexStoreData();
        }
    }

    class SummaryStoreRunnable implements Runnable {

        @Override
        public void run() {
            logger.info("[ScheduleApplication][SummaryStoreRunnable]Start at " + new Date());
            appSummaryService.appSummaryStoreData();
        }
    }
}