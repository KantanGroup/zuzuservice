package com.zuzuapps.task.app.jtests.repositories;

import com.zuzuapps.task.app.jtests.models.Word;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author tuanta17
 */
public interface WordRepository extends PagingAndSortingRepository<Word, Integer> {
}
