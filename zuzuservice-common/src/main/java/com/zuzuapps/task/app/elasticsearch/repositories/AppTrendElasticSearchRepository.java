package com.zuzuapps.task.app.elasticsearch.repositories;

import com.zuzuapps.task.app.elasticsearch.models.AppTrendElasticSearch;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author tuanta17
 */
public interface AppTrendElasticSearchRepository extends ElasticsearchRepository<AppTrendElasticSearch, String> {
}
