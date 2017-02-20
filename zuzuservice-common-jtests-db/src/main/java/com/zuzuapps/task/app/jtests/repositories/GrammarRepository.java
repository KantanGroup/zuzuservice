package com.jtests.api.repositories;

import com.jtests.api.models.Grammar;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

/**
 * @author tuanta17
 */
public interface GrammarRepository extends PagingAndSortingRepository<Grammar, Integer> {

    @RestResource(path = "allGrammar", rel = "allGrammar")
    List<Grammar> findAllByOrderById();
}
