package com.zuzuapps.task.app.elasticsearch.repositories;

import com.zuzuapps.task.app.elasticsearch.models.AppScreenshotElasticSearch;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author tuanta17
 */
public interface AppScreenshotElasticSearchRepository extends ElasticsearchRepository<AppScreenshotElasticSearch, String> {
}
