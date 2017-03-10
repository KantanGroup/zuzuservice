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

import com.zuzuapps.task.app.AppstoreCommonConfiguration;
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
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Date;

@SpringBootApplication
@Import({AppstoreCommonConfiguration.class})
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
    private AppCommonService appCommonService;
    @Autowired
    private AppIndexStoreService appIndexService;
    @Autowired
    private AppIndexService appTopService;
    @Autowired
    private AppSummaryService appSummaryService;
    @Autowired
    private AppSummaryStoreService appSummaryStoreService;
    @Autowired
    private AppInformationDailyService appInformationDailyService;
    @Autowired
    private AppInformationSummaryService appInformationSummaryService;
    @Autowired
    private AppScreenshotSummaryService appScreenshotSummaryService;
    @Autowired
    private AppScreenshotIndexService appScreenshotIndexService;
    @Autowired
    private AppService appService;

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(25);
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
                        executor.execute(new GenerationIndexRunnable());
                    }
                    executor.execute(new DailyIndexUpdateRunnable());
                    executor.execute(new DailyAppInformationUpdateRunnable());
                    if (isProcessDailyApp) {
                        executor.execute(new DailyAppUpdateRunnable());
                    }
                    if (isProcessDailyScreen) {
                        executor.execute(new ProcessAppScreenshotIndexRunnable());
                    }
                    executor.execute(new ProcessIndexStoreRunnable());
                }
                if (isProcessSummaryService) {
                    if (isProcessSummaryGenerate) {
                        executor.execute(new GenerationSummaryRunnable());
                    }
                    executor.execute(new SummaryIndexUpdateRunnable());
                    executor.execute(new SummaryAppInformationUpdateRunnable());
                    if (isProcessSummaryApp) {
                        executor.execute(new SummaryAppUpdateRunnable());
                    }
                    if (isProcessSummaryScreen) {
                        executor.execute(new ProcessAppScreenshotIndexRunnable());
                    }
                    executor.execute(new ProcessSummaryStoreRunnable());
                }
            }
        };
    }

    /**
     * Write top app info of category in to json
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void scheduleAppTop() {
        appTopService.generateAppIndexStore();
    }

    /**
     * Write app summary in USA to json
     */
    @Scheduled(cron = "0 0 0 1 * *")
    public void scheduleAppSummary() {
        if (isProcessSummaryGenerate) {
            appSummaryService.generateAppSummaryStore();
        }
    }

    class DailyIndexUpdateRunnable implements Runnable {

        @Override
        public void run() {
            logger.info("[ScheduleApplication][DailyIndexUpdateRunnable]Start at " + new Date());
            appIndexService.dailyAppIndexUpdate();
        }
    }

    class SummaryIndexUpdateRunnable implements Runnable {

        @Override
        public void run() {
            logger.info("[ScheduleApplication][SummaryIndexUpdateRunnable]Start at " + new Date());
            appSummaryStoreService.summaryAppIndexUpdate();
        }
    }

    class DailyAppInformationUpdateRunnable implements Runnable {

        @Override
        public void run() {
            logger.info("[ScheduleApplication][DailyAppInformationUpdateRunnable]Start at " + new Date());
            appInformationDailyService.dailyAppInformationUpdate();
        }
    }

    class SummaryAppInformationUpdateRunnable implements Runnable {

        @Override
        public void run() {
            logger.info("[ScheduleApplication][SummaryAppInformationUpdateRunnable]Start at " + new Date());
            appInformationSummaryService.summaryAppInformationUpdate();
        }
    }

    class DailyAppUpdateRunnable implements Runnable {

        @Override
        public void run() {
            logger.info("[ScheduleApplication][DailyAppUpdateRunnable]Start at " + new Date());
            appService.dailyAppUpdate();
        }
    }

    class SummaryAppUpdateRunnable implements Runnable {

        @Override
        public void run() {
            logger.info("[ScheduleApplication][SummaryAppUpdateRunnable]Start at " + new Date());
            appService.summaryAppUpdate();
        }
    }

    class ProcessAppScreenshotIndexRunnable implements Runnable {

        @Override
        public void run() {
            logger.info("[ScheduleApplication][ProcessAppScreenshotIndexRunnable]Start at " + new Date());
            appScreenshotIndexService.processAppScreenshotIndexUpdate();
        }
    }

    class ProcessAppScreenshotSummaryRunnable implements Runnable {

        @Override
        public void run() {
            logger.info("[ScheduleApplication][ProcessAppScreenshotSummaryRunnable]Start at " + new Date());
            appScreenshotSummaryService.processAppScreenshotUpdate();
        }
    }

    class GenerationIndexRunnable implements Runnable {

        @Override
        public void run() {
            logger.info("[ScheduleApplication][GenerationIndexRunnable]Start at " + new Date());
            appTopService.generateAppIndexStore();
        }
    }

    class GenerationSummaryRunnable implements Runnable {

        @Override
        public void run() {
            logger.info("[ScheduleApplication][GenerationSummaryRunnable]Start at " + new Date());
            appSummaryService.generateAppSummaryStore();
        }
    }

    class ProcessIndexStoreRunnable implements Runnable {

        @Override
        public void run() {
            logger.info("[ScheduleApplication][ProcessIndexStoreRunnable]Start at " + new Date());
            appTopService.processAppIndexStoreData();
        }
    }

    class ProcessSummaryStoreRunnable implements Runnable {

        @Override
        public void run() {
            logger.info("[ScheduleApplication][ProcessSummaryStoreRunnable]Start at " + new Date());
            appSummaryService.processAppSummaryStoreData();
        }
    }
}