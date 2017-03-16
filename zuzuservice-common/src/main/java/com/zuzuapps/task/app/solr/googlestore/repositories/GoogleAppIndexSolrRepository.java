package com.zuzuapps.task.app.solr.googlestore.repositories;

import com.zuzuapps.task.app.solr.googlestore.models.GoogleAppIndexSolr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.solr.repository.SolrCrudRepository;

/**
 * @author tuanta17
 */
public interface GoogleAppIndexSolrRepository extends SolrCrudRepository<GoogleAppIndexSolr, String> {
    Page<GoogleAppIndexSolr> findByCountryCodeAndCategoryAndCollectionOrderByIndexAsc(@Param("countryCode") String countryCode, @Param("category") String category, @Param("collection") String collection, Pageable pageable);
}
