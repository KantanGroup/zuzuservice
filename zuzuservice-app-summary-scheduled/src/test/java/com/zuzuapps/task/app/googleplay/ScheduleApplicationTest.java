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
import com.zuzuapps.task.app.common.DataServiceEnum;
import com.zuzuapps.task.app.common.DataTypeEnum;
import com.zuzuapps.task.app.elasticsearch.models.AppIndexElasticSearch;
import com.zuzuapps.task.app.elasticsearch.repositories.AppIndexElasticSearchRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.Iterator;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({GooglePlayCommonConfiguration.class})
public class ScheduleApplicationTest {
    @Value("${data.root.path:/tmp}")
    private String rootPath;
    @Autowired
    private AppIndexService scheduleService;
    @Autowired
    private AppSummaryService appSummaryService;
    @Autowired
    private AppIndexElasticSearchRepository appIndexElasticSearchRepository;

    @Test
    public void appTop() {
        scheduleService.appIndexStoreData();
    }

    @Test
    public void appSummary() {
        appSummaryService.appSummary();
    }

    @Test
    public void testProcessAppIndexQueue() {
        // something that should execute on weekdays only
        String time = CommonUtils.getDailyByTime();
        String dirPath = CommonUtils.folderBy(rootPath, DataServiceEnum.top.name(), DataTypeEnum.queue.name(), time).getAbsolutePath();
        File dir = new File(dirPath);
        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            scheduleService.processIndexUpdate(files);
        }
    }

    @Test
    public void testDataInElastichSearch() {
        Iterator<AppIndexElasticSearch> appIndexElasticSearches = appIndexElasticSearchRepository.findAll().iterator();
        while (appIndexElasticSearches.hasNext()) {
            AppIndexElasticSearch appIndexElasticSearch = appIndexElasticSearches.next();
            System.out.println(appIndexElasticSearch.getId());
            System.out.println(appIndexElasticSearch.getAppId());
        }
    }
}