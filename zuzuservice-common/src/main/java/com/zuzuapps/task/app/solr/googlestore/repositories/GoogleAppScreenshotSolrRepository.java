package com.zuzuapps.task.app.solr.googlestore.repositories;

import com.zuzuapps.task.app.solr.googlestore.models.GoogleAppScreenshotSolr;
import org.springframework.data.solr.repository.SolrCrudRepository;

/**
 * @author tuanta17
 */
public interface GoogleAppScreenshotSolrRepository extends SolrCrudRepository<GoogleAppScreenshotSolr, String> {
}
