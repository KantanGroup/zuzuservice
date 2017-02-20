package com.jtests.api.repositories;

import com.jtests.api.models.KanjiRadical;
import com.jtests.api.models.KanjiRadicalId;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author tuanta17
 */
public interface KanjiRadicalRepository extends PagingAndSortingRepository<KanjiRadical, KanjiRadicalId> {
}
