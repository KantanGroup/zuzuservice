package com.zuzuapps.task.app.jtests.repositories;

import com.zuzuapps.task.app.jtests.models.Grammar;
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
