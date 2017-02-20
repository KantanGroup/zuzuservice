package com.zuzuapps.task.app.jtests.repositories;

import com.zuzuapps.task.app.jtests.models.KanjiRadical;
import com.zuzuapps.task.app.jtests.models.KanjiRadicalId;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author tuanta17
 */
public interface KanjiRadicalRepository extends PagingAndSortingRepository<KanjiRadical, KanjiRadicalId> {
}
