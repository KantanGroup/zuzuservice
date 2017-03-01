package com.zuzuapps.task.app.solr.repositories;

import com.zuzuapps.task.app.solr.models.AppIndexSolr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.solr.repository.SolrCrudRepository;

import java.util.List;

/**
 * @author tuanta17
 */
public interface AppIndexSolrRepository extends SolrCrudRepository<AppIndexSolr, String> {
    Page<AppIndexSolr> findByCountryCodeAndCategoryAndCollection(@Param("countryCode") String countryCode, @Param("category") String category, @Param("collection") String collection, Pageable pageable);
}
