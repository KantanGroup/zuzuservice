package com.zuzuapps.task.app.jtests.repositories;

import com.zuzuapps.task.app.jtests.models.Sentence;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author tuanta17
 */
public interface SentenceRepository extends PagingAndSortingRepository<Sentence, Integer> {
}
