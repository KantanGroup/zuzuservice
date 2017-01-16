package com.zuzuapps.task.app.elasticsearch.repositories;

import com.zuzuapps.task.app.elasticsearch.models.AppIndexElasticSearch;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author tuanta17
 */
public interface AppIndexElasticSearchRepository extends ElasticsearchRepository<AppIndexElasticSearch, String> {
}
