package com.zuzuapps.api;

import com.zuzuapps.task.app.elasticsearch.models.AppIndexElasticSearch;
import com.zuzuapps.task.app.elasticsearch.repositories.AppIndexElasticSearchRepository;
import com.zuzuapps.task.app.solr.models.AppIndexSolr;
import com.zuzuapps.task.app.solr.repositories.AppIndexSolrRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author tuanta17
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AppIndexSolrRepositoryTest {
    @Autowired
    AppIndexSolrRepository repository;
    @Autowired
    AppIndexElasticSearchRepository appIndexElasticSearchRepository;

    @Test
    public void testGetIndex() throws Exception {
        AppIndexSolr app = new AppIndexSolr();
        app.setId("aaaaaa");
        app.setIndex(0);
        app.setAppId("aaaaaa");
        app.setTitle("aaaaaa");
        app.setCategory("aaaaaa");
        app.setCollection("aaaaaa");
        app.setCountryCode("aaaaaa");
        app.setIcon("aaaaaa");
        repository.save(app);
        Iterator<AppIndexSolr> apps = repository.findAll().iterator();
        while (apps.hasNext()) {
            AppIndexSolr app_ = apps.next();
            System.out.println(app_.getAppId());
        }
    }

    @Test
    public void moveElasticSearchToSolr() {
        List<AppIndexElasticSearch> apps = appIndexElasticSearchRepository.findByCountryCodeAndCategoryAndCollection("at", "game", "topselling_free");
        List<AppIndexSolr> index = new ArrayList<>();
        apps.forEach(app -> {
            AppIndexSolr index_ = new AppIndexSolr();
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
        repository.save(index);
    }

    @Test
    public void searchInSolr() {
        List<AppIndexSolr> apps = repository.findByCountryCodeAndCategoryAndCollection("at", "game", "topselling_free");
        System.out.println(apps.size());
    }
}
