package com.zuzuapps.task.app.jtests.repositories;

import com.zuzuapps.task.app.jtests.models.Name;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author tuanta17
 */
public interface NameRepository extends PagingAndSortingRepository<Name, Integer> {
}
