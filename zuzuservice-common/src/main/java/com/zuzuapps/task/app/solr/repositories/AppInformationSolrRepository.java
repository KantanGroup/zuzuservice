package com.zuzuapps.task.app.solr.repositories;

import com.zuzuapps.task.app.solr.models.AppInformationSolr;
import org.springframework.data.repository.query.Param;
import org.springframework.data.solr.repository.SolrCrudRepository;

import java.util.List;

/**
 * @author tuanta17
 */
public interface AppInformationSolrRepository extends SolrCrudRepository<AppInformationSolr, String> {
    List<AppInformationSolr> findBySummary(@Param("summary") String summary);
}
