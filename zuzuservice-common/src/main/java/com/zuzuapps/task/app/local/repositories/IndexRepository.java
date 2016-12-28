package com.zuzuapps.task.app.local.repositories;

import com.zuzuapps.task.app.local.models.ApplicationIndexLocal;
import org.springframework.data.repository.CrudRepository;

/**
 * @author tuanta17
 */
public interface IndexRepository extends CrudRepository<ApplicationIndexLocal, Long> {
}
