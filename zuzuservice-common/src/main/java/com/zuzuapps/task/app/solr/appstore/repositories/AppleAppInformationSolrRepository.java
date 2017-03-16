package com.zuzuapps.task.app.solr.appstore.repositories;

import com.zuzuapps.task.app.solr.appstore.models.AppleAppInformationSolr;
import org.springframework.data.solr.repository.SolrCrudRepository;

/**
 * @author tuanta17
 */
public interface AppleAppInformationSolrRepository extends SolrCrudRepository<AppleAppInformationSolr, Integer> {
}
