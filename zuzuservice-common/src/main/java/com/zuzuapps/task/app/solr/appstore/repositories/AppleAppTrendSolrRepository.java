package com.zuzuapps.task.app.solr.appstore.repositories;

import com.zuzuapps.task.app.solr.appstore.models.AppleAppTrendSolr;
import org.springframework.data.solr.repository.SolrCrudRepository;

/**
 * @author tuanta17
 */
public interface AppleAppTrendSolrRepository extends SolrCrudRepository<AppleAppTrendSolr, String> {
}
