package com.zuzuapps.task.app.elasticsearch.repositories;

import com.zuzuapps.task.app.elasticsearch.models.AppIndexElasticSearch;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author tuanta17
 */
public interface AppIndexElasticSearchRepository extends ElasticsearchRepository<AppIndexElasticSearch, String> {
    List<AppIndexElasticSearch> findByCountryCodeAndCategoryAndCollection(@Param("countryCode") String countryCode, @Param("category") String category, @Param("collection") String collection);
}
