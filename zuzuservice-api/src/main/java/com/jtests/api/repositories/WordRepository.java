package com.jtests.api.repositories;

import com.jtests.api.models.Word;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author tuanta17
 */
public interface WordRepository extends PagingAndSortingRepository<Word, Integer> {
}
