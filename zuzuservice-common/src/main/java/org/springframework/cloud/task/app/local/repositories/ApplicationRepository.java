package org.springframework.cloud.task.app.local.repositories;

import org.springframework.cloud.task.app.local.models.ApplicationLocal;
import org.springframework.data.repository.CrudRepository;

/**
 * @author tuanta17
 */
public interface ApplicationRepository extends CrudRepository<ApplicationLocal, String> {
}
