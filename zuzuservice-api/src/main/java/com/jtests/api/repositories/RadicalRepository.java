package com.jtests.api.repositories;

import com.jtests.api.models.Radical;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author tuanta17
 */
public interface RadicalRepository extends PagingAndSortingRepository<Radical, Integer> {
}
