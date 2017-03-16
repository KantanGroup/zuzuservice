package com.zuzuapps.task.app.solr.googlestore.repositories;

import com.zuzuapps.task.app.solr.googlestore.models.GoogleAppInformationSolr;
import org.springframework.data.repository.query.Param;
import org.springframework.data.solr.repository.SolrCrudRepository;

import java.util.List;

/**
 * @author tuanta17
 */
public interface GoogleAppInformationSolrRepository extends SolrCrudRepository<GoogleAppInformationSolr, String> {
    List<GoogleAppInformationSolr> findBySummary(@Param("summary") String summary);
}
