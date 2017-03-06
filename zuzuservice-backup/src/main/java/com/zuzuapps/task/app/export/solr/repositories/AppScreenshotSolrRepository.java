package com.zuzuapps.task.app.export.solr.repositories;

import com.zuzuapps.task.app.export.solr.models.AppScreenshotSolr;
import org.springframework.data.solr.repository.SolrCrudRepository;

/**
 * @author tuanta17
 */
public interface AppScreenshotSolrRepository extends SolrCrudRepository<AppScreenshotSolr, String> {
}
