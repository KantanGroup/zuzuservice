package com.zuzuapps.task.app.googleplay.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import com.zuzuapps.task.app.GooglePlayCommonConfiguration;
import com.zuzuapps.task.app.common.CategoryEnum;
import com.zuzuapps.task.app.common.CollectionEnum;
import com.zuzuapps.task.app.exceptions.GooglePlayRuntimeException;
import com.zuzuapps.task.app.googleplay.models.ApplicationPlays;
import com.zuzuapps.task.app.googleplay.servies.ApplicationPlayService;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author tuanta17
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(GooglePlayCommonConfiguration.class)
public class ApplicationPlayServiceTest {
    @Autowired
    ApplicationPlayService applicationPlayService;

    @Test
    public void testGetApplicationByPage() throws GooglePlayRuntimeException, JsonProcessingException {
        ApplicationPlays applications = applicationPlayService.getApplications(CategoryEnum.GAME, CollectionEnum.topselling_free, "vi", "vn", 0);
        Assert.assertEquals(60,applications.getResults().size());
        final ObjectMapper mapper = new ObjectMapper();
        System.out.println(mapper.writeValueAsString(applications));
    }
}
