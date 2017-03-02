package com.zuzuapps.task.app.solr.repositories;

import com.zuzuapps.task.app.solr.models.AppTrendSolr;
import org.springframework.data.repository.query.Param;
import org.springframework.data.solr.repository.SolrCrudRepository;

import java.util.List;

/**
 * @author tuanta17
 */
public interface AppTrendSolrRepository extends SolrCrudRepository<AppTrendSolr, String> {
    List<AppTrendSolr> findByCountryCodeAndCategoryAndCollectionAndAppIdOrderByCreateAtDesc(@Param("countryCode") String countryCode, @Param("category") String category, @Param("collection") String collection, @Param("appId") String appId);

    List<AppTrendSolr> findByCountryCodeAndAppIdOrderByCreateAtDesc(@Param("countryCode") String countryCode, @Param("appId") String appId);

    List<AppTrendSolr> findByCountryCodeAndCategoryAndCollectionAndIndexOrderByCreateAtDesc(@Param("countryCode") String countryCode, @Param("category") String category, @Param("collection") String collection, @Param("index") int index);
}
