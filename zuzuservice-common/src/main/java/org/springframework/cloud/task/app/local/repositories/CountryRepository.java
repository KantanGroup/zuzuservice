package org.springframework.cloud.task.app.local.repositories;

import org.springframework.cloud.task.app.local.models.CountryLocal;
import org.springframework.data.repository.CrudRepository;

/**
 * @author tuanta17
 */
public interface CountryRepository extends CrudRepository<CountryLocal, Long> {
}