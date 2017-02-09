package com.jtests.api.repositories;

import com.jtests.api.models.Name;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author tuanta17
 */
public interface NameRepository extends PagingAndSortingRepository<Name, Integer> {
}
