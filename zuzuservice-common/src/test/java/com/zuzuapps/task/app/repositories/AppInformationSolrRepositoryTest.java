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
    public void testGetAppNotFound() {
        // System.out.println(appInformationSolrRepository.findBySummary("App not found").size());
        appInformationSolrRepository.delete(appInformationSolrRepository.findBySummary("App not found"));
    }

    @Test
    public void testGetAppTrend() {
        System.out.println("Get app trend");
        List<AppTrendSolr> unmodifiableList = appTrendSolrRepository.findByCountryCodeAndCategoryAndCollectionAndAppId("jp", "all", "topselling_free", "jp.konami.duellinks");
        //List<AppTrendSolr> unmodifiableList = appTrendSolrRepository.findByCountryCodeAndCategoryAndCollectionAndAppId("au", "books_and_reference", "topselling_free", "com.audible.application");
        List<AppTrendSolr> modifiableList =  new ArrayList<AppTrendSolr>(unmodifiableList);
        System.out.println(modifiableList.size());
        if (modifiableList != null && !modifiableList.isEmpty()) {
            Collections.sort(modifiableList, new Comparator<AppTrendSolr>() {
                public int compare(AppTrendSolr o1, AppTrendSolr o2) {
                    return o1.getCreateAt().compareTo(o2.getCreateAt());
                }
            });
        }
        for(AppTrendSolr app: modifiableList) {
            System.out.println(CommonUtils.getTimeBy(app.getCreateAt(), "yyyyMMdd") + "\t\t" + app.getAppId() + "\t\t" + app.getIndex());
        }
    }

    @Test
    public void testGetAppTrendOfCategory() {
        System.out.println("Get app trend of category");
        List<AppTrendSolr> unmodifiableList = appTrendSolrRepository.findByCountryCodeAndCategoryAndCollectionAndIndex("jp", "game", "topselling_free", 1);
        //List<AppTrendSolr> unmodifiableList = appTrendSolrRepository.findByCountryCodeAndCategoryAndCollectionAndAppId("au", "books_and_reference", "topselling_free", "com.audible.application");
        List<AppTrendSolr> modifiableList =  new ArrayList<AppTrendSolr>(unmodifiableList);
        System.out.println(modifiableList.size());
        if (modifiableList != null && !modifiableList.isEmpty()) {
            Collections.sort(modifiableList, new Comparator<AppTrendSolr>() {
                public int compare(AppTrendSolr o1, AppTrendSolr o2) {
                    return o1.getCreateAt().compareTo(o2.getCreateAt());
                }
            });
        }
        for(AppTrendSolr app: modifiableList) {
            System.out.println(CommonUtils.getTimeBy(app.getCreateAt(), "yyyyMMdd") + "\t\t" + app.getAppId() + "\t\t" + app.getIndex());
        }
    }
}
