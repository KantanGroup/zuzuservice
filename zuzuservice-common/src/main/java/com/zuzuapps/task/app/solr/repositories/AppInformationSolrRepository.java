package com.zuzuapps.task.app.solr.repositories;

import com.zuzuapps.task.app.solr.models.AppInformationSolr;
import org.springframework.data.solr.repository.SolrCrudRepository;

/**
 * @author tuanta17
 */
public interface AppInformationSolrRepository extends SolrCrudRepository<AppInformationSolr, String> {
}
