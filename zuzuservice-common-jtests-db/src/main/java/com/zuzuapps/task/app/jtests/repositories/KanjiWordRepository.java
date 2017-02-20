package com.zuzuapps.task.app.jtests.repositories;

import com.zuzuapps.task.app.jtests.models.KanjiWord;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author tuanta17
 */
public interface KanjiWordRepository extends PagingAndSortingRepository<KanjiWord, Integer> {
}
