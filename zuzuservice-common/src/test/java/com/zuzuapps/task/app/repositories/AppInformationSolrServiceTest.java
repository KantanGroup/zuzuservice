package com.zuzuapps.task.app.repositories;

import com.zuzuapps.task.app.solr.services.AppInformationSolrService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author tuanta17
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AppInformationSolrServiceTest {
    @Autowired
    AppInformationSolrService appInformatioService;

    @Test
    public void testFindTopApplication() throws Exception {
        appInformatioService.findTopApplication(1000000000l, 4999999999l);
    }
}
