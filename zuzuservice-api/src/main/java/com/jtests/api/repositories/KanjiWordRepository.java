package com.jtests.api.repositories;

import com.jtests.api.models.KanjiWord;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author tuanta17
 */
public interface KanjiWordRepository extends PagingAndSortingRepository<KanjiWord, Integer> {
}
