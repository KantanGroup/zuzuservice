package com.zuzuapps.task.app.solr.appstore.repositories;

import com.zuzuapps.task.app.solr.appstore.models.AppleAppIndexSolr;
import org.springframework.data.solr.repository.SolrCrudRepository;

/**
 * @author tuanta17
 */
public interface AppleAppIndexSolrRepository extends SolrCrudRepository<AppleAppIndexSolr, Integer> {
}
