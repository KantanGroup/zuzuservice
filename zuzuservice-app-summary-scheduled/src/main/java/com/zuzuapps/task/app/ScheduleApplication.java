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

package com.zuzuapps.task.app;

import com.zuzuapps.task.app.googlestore.*;
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
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Date;

@SpringBootApplication
@Import({AppstoreCommonConfiguration.class})
@EnableScheduling
public class ScheduleApplication {
    final Log logger = LogFactory.getLog("ScheduleApplication");
    @Value("${process.daily.service:/false}")
    protected boolean isProcessDailyService;
    @Value("${process.daily.screen:/false}")
    protected boolean isProcessDailyScreen;
    @Value("${process.daily.generate:/false}")
    protected boolean isProcessDailyGenerate;
    @Value("${process.daily.app:/false}")
    protected boolean isProcessDailyApp;
    @Value("${process.summary.service:/false}")
    protected boolean isProcessSummaryService;
    @Value("${process.summary.screen:/false}")
    protected boolean isProcessSummaryScreen;
    @Value("${process.summary.generate:/false}")
    protected boolean isProcessSummaryGenerate;
    @Value("${process.summary.app:/false}")
    protected boolean isProcessSummaryApp;
    @Value("${data.root.path:/tmp}")
    private String rootPath;
    @Autowired
    private GoogleAppCommonService googleAppCommonService;
    @Autowired
    private GoogleAppIndexStoreService googleAppIndexStoreService;
    @Autowired
    private GoogleAppIndexService googleAppIndexService;
    @Autowired
    private GoogleAppSummaryService googleAppSummaryService;
    @Autowired
    private GoogleAppSummaryStoreService googleAppSummaryStoreService;
    @Autowired
    private GoogleAppInformationDailyService googleAppInformationDailyService;
    @Autowired
    private GoogleAppInformationSummaryService googleAppInformationSummaryService;
    @Autowired
    private GoogleAppScreenshotSummaryService googleAppScreenshotSummaryService;
    @Autowired
    private GoogleAppScreenshotIndexService googleAppScreenshotIndexService;
    @Autowired
    private GoogleAppService googleAppService;

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        return executor;
    }

    public static void main(String[] args) {
        SpringApplication.run(ScheduleApplication.class, args);
    }

    @Bean
    public CommandLineRunner schedulingRunner(final TaskExecutor executor) {
        return new CommandLineRunner() {
            public void run(String... args) throws Exception {
                if (isProcessDailyService) {
                    if (isProcessDailyGenerate) {
                        executor.execute(new GoogleGenerationIndexRunnable());
                    }
                    executor.execute(new GoogleDailyIndexUpdateRunnable());
                    executor.execute(new GoogleDailyAppInformationUpdateRunnable());
                    if (isProcessDailyApp) {
                        executor.execute(new GoogleDailyAppUpdateRunnable());
                    }
                    if (isProcessDailyScreen) {
                        executor.execute(new GoogleProcessAppScreenshotIndexRunnable());
                    }
                    executor.execute(new GoogleProcessIndexStoreRunnable());
                }
                if (isProcessSummaryService) {
                    if (isProcessSummaryGenerate) {
                        executor.execute(new GoogleGenerationSummaryRunnable());
                    }
                    executor.execute(new GoogleSummaryIndexUpdateRunnable());
                    executor.execute(new GoogleSummaryAppInformationUpdateRunnable());
                    if (isProcessSummaryApp) {
                        executor.execute(new GoogleSummaryAppUpdateRunnable());
                    }
                    if (isProcessSummaryScreen) {
                        executor.execute(new GoogleProcessAppScreenshotIndexRunnable());
                    }
                    executor.execute(new GoogleProcessSummaryStoreRunnable());
                }
            }
        };
    }

    /**
     * Write top app info of category in to json
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void scheduleGoogleAppTrend() {
        googleAppIndexService.generateAppIndexStore();
    }

    /**
     * Write app summary in USA to json
     */
    @Scheduled(cron = "0 0 0 1 * *")
    public void scheduleGoogleAppSummary() {
        if (isProcessSummaryGenerate) {
            googleAppSummaryService.generateAppSummaryStore();
        }
    }

    class GoogleDailyIndexUpdateRunnable implements Runnable {

        @Override
        public void run() {
            logger.info("[ScheduleApplication][GoogleDailyIndexUpdateRunnable]Start at " + new Date());
            googleAppIndexStoreService.dailyAppIndexUpdate();
        }
    }

    class GoogleSummaryIndexUpdateRunnable implements Runnable {

        @Override
        public void run() {
            logger.info("[ScheduleApplication][GoogleSummaryIndexUpdateRunnable]Start at " + new Date());
            googleAppSummaryStoreService.summaryAppIndexUpdate();
        }
    }

    class GoogleDailyAppInformationUpdateRunnable implements Runnable {

        @Override
        public void run() {
            logger.info("[ScheduleApplication][GoogleDailyAppInformationUpdateRunnable]Start at " + new Date());
            googleAppInformationDailyService.dailyAppInformationUpdate();
        }
    }

    class GoogleSummaryAppInformationUpdateRunnable implements Runnable {

        @Override
        public void run() {
            logger.info("[ScheduleApplication][GoogleSummaryAppInformationUpdateRunnable]Start at " + new Date());
            googleAppInformationSummaryService.summaryAppInformationUpdate();
        }
    }

    class GoogleDailyAppUpdateRunnable implements Runnable {

        @Override
        public void run() {
            logger.info("[ScheduleApplication][GoogleDailyAppUpdateRunnable]Start at " + new Date());
            googleAppService.dailyAppUpdate();
        }
    }

    class GoogleSummaryAppUpdateRunnable implements Runnable {

        @Override
        public void run() {
            logger.info("[ScheduleApplication][GoogleSummaryAppUpdateRunnable]Start at " + new Date());
            googleAppService.summaryAppUpdate();
        }
    }

    class GoogleProcessAppScreenshotIndexRunnable implements Runnable {

        @Override
        public void run() {
            logger.info("[ScheduleApplication][GoogleProcessAppScreenshotIndexRunnable]Start at " + new Date());
            googleAppScreenshotIndexService.processAppScreenshotIndexUpdate();
        }
    }

    class GoogleProcessAppScreenshotSummaryRunnable implements Runnable {

        @Override
        public void run() {
            logger.info("[ScheduleApplication][GoogleProcessAppScreenshotSummaryRunnable]Start at " + new Date());
            googleAppScreenshotSummaryService.processAppScreenshotUpdate();
        }
    }

    class GoogleGenerationIndexRunnable implements Runnable {

        @Override
        public void run() {
            logger.info("[ScheduleApplication][GoogleGenerationIndexRunnable]Start at " + new Date());
            googleAppIndexService.generateAppIndexStore();
        }
    }

    class GoogleGenerationSummaryRunnable implements Runnable {

        @Override
        public void run() {
            logger.info("[ScheduleApplication][GoogleGenerationSummaryRunnable]Start at " + new Date());
            googleAppSummaryService.generateAppSummaryStore();
        }
    }

    class GoogleProcessIndexStoreRunnable implements Runnable {

        @Override
        public void run() {
            logger.info("[ScheduleApplication][GoogleProcessIndexStoreRunnable]Start at " + new Date());
            googleAppIndexService.processAppIndexStoreData();
        }
    }

    class GoogleProcessSummaryStoreRunnable implements Runnable {

        @Override
        public void run() {
            logger.info("[ScheduleApplication][GoogleProcessSummaryStoreRunnable]Start at " + new Date());
            googleAppSummaryService.processAppSummaryStoreData();
        }
    }
}