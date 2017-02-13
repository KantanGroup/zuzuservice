package com.zuzuapps.task.app.solr.repositories;

import com.zuzuapps.task.app.solr.models.AppScreenshotSolr;
import org.springframework.data.solr.repository.SolrCrudRepository;

/**
 * @author tuanta17
 */
public interface AppScreenshotSolrRepository extends SolrCrudRepository<AppScreenshotSolr, String> {
}
