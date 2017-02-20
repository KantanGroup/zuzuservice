package com.zuzuapps.task.app.jtests.repositories;

import com.zuzuapps.task.app.jtests.models.WordSentence;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author tuanta17
 */
public interface WordSentenceRepository extends PagingAndSortingRepository<WordSentence, Integer> {
}
