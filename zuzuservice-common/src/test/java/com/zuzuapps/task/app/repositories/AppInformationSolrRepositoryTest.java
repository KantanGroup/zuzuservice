package com.zuzuapps.task.app.repositories;

import com.zuzuapps.task.app.common.CommonUtils;
import com.zuzuapps.task.app.solr.models.AppTrendSolr;
import com.zuzuapps.task.app.solr.repositories.AppIndexSolrRepository;
import com.zuzuapps.task.app.solr.repositories.AppInformationSolrRepository;
import com.zuzuapps.task.app.solr.repositories.AppTrendSolrRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author tuanta17
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AppInformationSolrRepositoryTest {
    @Autowired
    AppInformationSolrRepository appInformationSolrRepository;
    @Autowired
    AppIndexSolrRepository appIndexSolrRepository;
    @Autowired
    AppTrendSolrRepository appTrendSolrRepository;

    @Test
    public void testGetApp() {
        System.out.println(appInformationSolrRepository.count());
        System.out.println(appIndexSolrRepository.count());
        System.out.println(appTrendSolrRepository.count());
    }

    @Test
    public void testGetAppTrend() {
        List<AppTrendSolr> data = appTrendSolrRepository.findByCountryCodeAndCategoryAndCollectionAndAppId("jp", "all", "topselling_free", "jp.konami.duellinks");
        //List<AppTrendSolr> data = appTrendSolrRepository.findByCountryCodeAndCategoryAndCollectionAndAppId("au", "books_and_reference", "topselling_free", "com.audible.application");
        List<AppTrendSolr> trends =  new ArrayList<AppTrendSolr>(data);
        System.out.println(trends.size());
        if (trends != null && !trends.isEmpty()) {
            Collections.sort(trends, new Comparator<AppTrendSolr>() {
                public int compare(AppTrendSolr o1, AppTrendSolr o2) {
                    return o1.getCreateAt().compareTo(o2.getCreateAt());
                }
            });
        }
        for(AppTrendSolr app: trends) {
            System.out.println(CommonUtils.getTimeBy(app.getCreateAt(), "yyyyMMdd") + "\t\t" + app.getAppId() + "\t\t" + app.getIndex());
        }
    }
}
