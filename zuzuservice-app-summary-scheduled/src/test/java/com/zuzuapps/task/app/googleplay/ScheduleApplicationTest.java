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

import com.zuzuapps.task.app.common.CommonUtils;
import com.zuzuapps.task.app.common.DataServiceEnum;
import com.zuzuapps.task.app.common.DataTypeEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ScheduleApplicationTest {
    @Value("${data.root.path:/tmp}")
    private String rootPath;
    @Autowired
    private AppIndexStoreService scheduleService;
    @Autowired
    private AppIndexService topScheduleService;
    @Autowired
    private AppSummaryService appSummaryService;

    @Test
    public void appTop() {
        topScheduleService.processAppIndexStoreData();
    }

    @Test
    public void testGenerateAppSummaryStore() {
        appSummaryService.generateAppSummaryStore();
        appSummaryService.processAppSummaryStoreData();
    }

    @Test
    public void testProcessAppIndexQueue() {
        // something that should execute on weekdays only
        String time = CommonUtils.getDailyByTime();
        String dirPath = CommonUtils.folderBy(rootPath, DataServiceEnum.top_app_daily.name(), DataTypeEnum.queue.name(), time).getAbsolutePath();
        File dir = new File(dirPath);
        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            try {
                scheduleService.processIndexUpdate(files);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}