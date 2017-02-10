package com.zuzuapps.api;

import com.zuzuapps.task.app.elasticsearch.models.AppIndexElasticSearch;
import com.zuzuapps.task.app.elasticsearch.repositories.AppIndexElasticSearchRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author tuanta17
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AppIndexElasticSearchRepositoryTest {
    @Autowired
    AppIndexElasticSearchRepository repository;

    @Test
    public void testGetIndex() throws Exception {
        List<AppIndexElasticSearch> apps = repository.findByCountryCodeAndCategoryAndCollection("at", "game", "topselling_free");
        System.out.println(apps.size());
    }
}
