package com.zuzuapps.task.app.elasticsearch.repositories;

import com.zuzuapps.task.app.elasticsearch.models.AppInformationElasticSearch;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author tuanta17
 */
public interface AppInformationElasticSearchRepository extends ElasticsearchRepository<AppInformationElasticSearch, String> {
}
