package com.zuzuapps.task.app.logs.repositories;

import com.zuzuapps.task.app.logs.models.AppLog;
import org.springframework.data.repository.CrudRepository;

/**
 * @author tuanta17
 */
public interface AppLogRepository extends CrudRepository<AppLog, String> {
}
