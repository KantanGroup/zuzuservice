package com.zuzuapps.task.app.elasticsearch.repositories;

import com.zuzuapps.task.app.elasticsearch.models.IndexElasticSearch;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author tuanta17
 */
public interface IndexElasticSearchRepository extends ElasticsearchRepository<IndexElasticSearch, String> {
    //@Query("{\"bool\":{\"must\":[{\"term\":{\"countryCode\":\"?0\"}},{\"term\":{\"category\":\"?1\"}},{\"term\":{\"collection\":\"?2\"}}]}}")
    List<IndexElasticSearch> findByCountryCodeAndCategoryAndCollection(@Param("countryCode") String countryCode, @Param("category") String category, @Param("collection") String collection);
}
