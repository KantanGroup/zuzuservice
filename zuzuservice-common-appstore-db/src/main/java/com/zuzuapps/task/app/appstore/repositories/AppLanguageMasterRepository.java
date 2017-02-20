package com.zuzuapps.task.app.appstore.repositories;

import com.zuzuapps.task.app.appstore.models.AppLanguageId;
import com.zuzuapps.task.app.appstore.models.AppLanguageMaster;
import org.springframework.data.repository.CrudRepository;

/**
 * @author tuanta17
 */
public interface AppLanguageMasterRepository extends CrudRepository<AppLanguageMaster, AppLanguageId> {
}
