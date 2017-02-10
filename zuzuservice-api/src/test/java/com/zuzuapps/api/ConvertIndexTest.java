package com.zuzuapps.api;

import com.zuzuapps.task.app.elasticsearch.models.AppIndexElasticSearch;
import com.zuzuapps.task.app.elasticsearch.models.IndexElasticSearch;
import com.zuzuapps.task.app.elasticsearch.repositories.AppIndexElasticSearchRepository;
import com.zuzuapps.task.app.elasticsearch.repositories.IndexElasticSearchRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tuanta17
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ConvertIndexTest {
    @Autowired
    AppIndexElasticSearchRepository appIndexElasticSearchRepository;
    @Autowired
    IndexElasticSearchRepository indexElasticSearchRepository;

    @Test
    public void testGetIndex() throws Exception {
        List<AppIndexElasticSearch> apps = appIndexElasticSearchRepository.findByCountryCodeAndCategoryAndCollection("at", "game", "topselling_free");
        List<IndexElasticSearch> index = new ArrayList<>();
        apps.forEach(app -> {
            IndexElasticSearch index_ = new IndexElasticSearch();
            index_.setAppId(app.getAppId());
            index_.setId(app.getId());
            index_.setCategory(app.getCategory());
            index_.setCollection(app.getCollection());
            index_.setCountryCode(app.getCountryCode());
            index_.setIcon(app.getIcon());
            index_.setTitle(app.getTitle());
            index_.setIndex(app.getIndex());
            index.add(index_);
        });
        indexElasticSearchRepository.save(index);
    }
}
