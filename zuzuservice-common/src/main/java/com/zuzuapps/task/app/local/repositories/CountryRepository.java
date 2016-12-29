package com.zuzuapps.task.app.local.repositories;

import com.zuzuapps.task.app.local.models.CountryLocal;
import org.springframework.data.repository.CrudRepository;

/**
 * @author tuanta17
 */
public interface CountryRepository extends CrudRepository<CountryLocal, Long> {
}