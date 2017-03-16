package com.zuzuapps.task.app.solr.googlestore.repositories;

import com.zuzuapps.task.app.solr.googlestore.models.GoogleAppTrendSolr;
import org.springframework.data.repository.query.Param;
import org.springframework.data.solr.repository.SolrCrudRepository;

import java.util.List;

/**
 * @author tuanta17
 */
public interface GoogleAppTrendSolrRepository extends SolrCrudRepository<GoogleAppTrendSolr, String> {
    List<GoogleAppTrendSolr> findByCountryCodeAndCategoryAndCollectionAndAppId(@Param("countryCode") String countryCode, @Param("category") String category, @Param("collection") String collection, @Param("appId") String appId);

    List<GoogleAppTrendSolr> findByCountryCodeAndCategoryAndCollectionAndAppIdOrderByCreateAtDesc(@Param("countryCode") String countryCode, @Param("category") String category, @Param("collection") String collection, @Param("appId") String appId);

    List<GoogleAppTrendSolr> findByCountryCodeAndAppId(@Param("countryCode") String countryCode, @Param("appId") String appId);

    List<GoogleAppTrendSolr> findByCountryCodeAndAppIdOrderByCreateAtDesc(@Param("countryCode") String countryCode, @Param("appId") String appId);

    List<GoogleAppTrendSolr> findByCountryCodeAndCategoryAndCollectionAndIndex(@Param("countryCode") String countryCode, @Param("category") String category, @Param("collection") String collection, @Param("index") int index);

    List<GoogleAppTrendSolr> findByCountryCodeAndCategoryAndCollectionAndIndexOrderByCreateAtDesc(@Param("countryCode") String countryCode, @Param("category") String category, @Param("collection") String collection, @Param("index") int index);
}
