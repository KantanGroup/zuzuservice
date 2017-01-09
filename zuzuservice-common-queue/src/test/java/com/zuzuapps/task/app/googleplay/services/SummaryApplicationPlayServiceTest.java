package com.zuzuapps.task.app.googleplay.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zuzuapps.task.app.GooglePlayCommonConfiguration;
import com.zuzuapps.task.app.common.CategoryEnum;
import com.zuzuapps.task.app.common.CollectionEnum;
import com.zuzuapps.task.app.exceptions.GooglePlayRuntimeException;
import com.zuzuapps.task.app.googleplay.models.SummaryApplicationPlay;
import com.zuzuapps.task.app.googleplay.models.SummaryApplicationPlays;
import com.zuzuapps.task.app.googleplay.servies.SummaryApplicationPlayService;
import com.zuzuapps.task.app.queue.models.AppIndexQueue;
import com.zuzuapps.task.app.queue.repositories.AppIndexQueueRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author tuanta17
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(GooglePlayCommonConfiguration.class)
public class SummaryApplicationPlayServiceTest {
    @Autowired
    SummaryApplicationPlayService applicationPlayService;
    @Autowired
    AppIndexQueueRepository indexRepository;

    @Test
    public void testGetApplicationByPage() throws GooglePlayRuntimeException, JsonProcessingException {
        SummaryApplicationPlays applications = applicationPlayService.getSummaryApplications(CategoryEnum.GAME, CollectionEnum.topselling_free, "vi", "vn", 0);
        Assert.assertEquals(60, applications.getResults().size());
        final ObjectMapper mapper = new ObjectMapper();
        List<AppIndexQueue> indexs = new ArrayList<>();
        int i = 0;
        for (SummaryApplicationPlay summary : applications.getResults()) {
            AppIndexQueue indexLocal = new AppIndexQueue();
            indexLocal.setAppId(summary.getAppId());
            indexLocal.setCategory(CategoryEnum.GAME);
            indexLocal.setCollection(CollectionEnum.topselling_free);
            indexLocal.setCountryCode("vn");
            indexLocal.setIndex(++i);
            indexLocal.setCreateAt(new Date());
            indexLocal.setUpdateAt(new Date());
            indexs.add(indexLocal);
        }
        indexRepository.save(indexs);
        System.out.println(mapper.writeValueAsString(applications));
    }
}
