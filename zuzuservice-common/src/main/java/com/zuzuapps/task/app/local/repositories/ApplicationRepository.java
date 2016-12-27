package com.zuzuapps.task.app.local.repositories;

import com.zuzuapps.task.app.local.models.ApplicationLocal;
import org.springframework.data.repository.CrudRepository;

/**
 * @author tuanta17
 */
public interface ApplicationRepository extends CrudRepository<ApplicationLocal, String> {
}
