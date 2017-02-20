package com.jtests.api.repositories;

import com.jtests.api.models.Sentence;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author tuanta17
 */
public interface SentenceRepository extends PagingAndSortingRepository<Sentence, Integer> {
}
