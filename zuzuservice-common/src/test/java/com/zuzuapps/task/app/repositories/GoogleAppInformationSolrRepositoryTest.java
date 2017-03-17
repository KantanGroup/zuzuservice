package com.zuzuapps.task.app.repositories;

import com.zuzuapps.task.app.common.CommonUtils;
import com.zuzuapps.task.app.solr.googlestore.models.GoogleAppTrendSolr;
import com.zuzuapps.task.app.solr.googlestore.repositories.GoogleAppIndexSolrRepository;
import com.zuzuapps.task.app.solr.googlestore.repositories.GoogleAppInformationSolrRepository;
import com.zuzuapps.task.app.solr.googlestore.repositories.GoogleAppTrendSolrRepository;
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
public class GoogleAppInformationSolrRepositoryTest {
    @Autowired
    GoogleAppInformationSolrRepository appInformationSolrRepository;
    @Autowired
    GoogleAppIndexSolrRepository appIndexSolrRepository;
    @Autowired
    GoogleAppTrendSolrRepository appTrendSolrRepository;

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
        List<GoogleAppTrendSolr> unmodifiableList = appTrendSolrRepository.findByCountryCodeAndCategoryAndCollectionAndAppIdOrderByCreateAtDesc("jp", "all", "topselling_free", "jp.konami.duellinks");
        //List<AppTrendSolr> unmodifiableList = appTrendSolrRepository.findByCountryCodeAndCategoryAndCollectionAndAppId("au", "books_and_reference", "topselling_free", "com.audible.application");
        List<GoogleAppTrendSolr> modifiableList = new ArrayList<GoogleAppTrendSolr>(unmodifiableList);
        System.out.println(modifiableList.size());
        if (modifiableList != null && !modifiableList.isEmpty()) {
            Collections.sort(modifiableList, new Comparator<GoogleAppTrendSolr>() {
                public int compare(GoogleAppTrendSolr o1, GoogleAppTrendSolr o2) {
                    return o1.getCreateAt().compareTo(o2.getCreateAt());
                }
            });
        }
        for (GoogleAppTrendSolr app : modifiableList) {
            System.out.println(CommonUtils.getTimeBy(app.getCreateAt(), "yyyyMMdd") + "\t\t" + app.getAppId() + "\t\t" + app.getIndex());
        }
    }

    @Test
    public void testGetAppTrendOfCategory() {
        System.out.println("Get app trend of category");
        List<GoogleAppTrendSolr> unmodifiableList = appTrendSolrRepository.findByCountryCodeAndCategoryAndCollectionAndIndexOrderByCreateAtDesc("jp", "game", "topselling_free", 1);
        //List<AppTrendSolr> unmodifiableList = appTrendSolrRepository.findByCountryCodeAndCategoryAndCollectionAndAppId("au", "books_and_reference", "topselling_free", "com.audible.application");
        List<GoogleAppTrendSolr> modifiableList = new ArrayList<GoogleAppTrendSolr>(unmodifiableList);
        System.out.println(modifiableList.size());
        if (modifiableList != null && !modifiableList.isEmpty()) {
            Collections.sort(modifiableList, new Comparator<GoogleAppTrendSolr>() {
                public int compare(GoogleAppTrendSolr o1, GoogleAppTrendSolr o2) {
                    return o1.getCreateAt().compareTo(o2.getCreateAt());
                }
            });
        }
        for (GoogleAppTrendSolr app : modifiableList) {
            System.out.println(CommonUtils.getTimeBy(app.getCreateAt(), "yyyyMMdd") + "\t\t" + app.getAppId() + "\t\t" + app.getIndex());
        }
    }
}
