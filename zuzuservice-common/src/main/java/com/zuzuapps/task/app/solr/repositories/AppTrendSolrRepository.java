package com.zuzuapps.task.app.solr.repositories;

import com.zuzuapps.task.app.solr.models.AppTrendSolr;
import org.springframework.data.solr.repository.SolrCrudRepository;

/**
 * @author tuanta17
 */
public interface AppTrendSolrRepository extends SolrCrudRepository<AppTrendSolr, String> {
}
