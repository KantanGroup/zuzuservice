package com.jtests.api.repositories;

import com.jtests.api.models.WordSentence;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author tuanta17
 */
public interface WordSentenceRepository extends PagingAndSortingRepository<WordSentence, Integer> {
}
