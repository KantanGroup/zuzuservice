package com.zuzuapps.task.app.master.repositories;

import com.zuzuapps.task.app.master.models.AppLanguageId;
import com.zuzuapps.task.app.master.models.AppLanguageMaster;
import org.springframework.data.repository.CrudRepository;

/**
 * @author tuanta17
 */
public interface AppLanguageMasterRepository extends CrudRepository<AppLanguageMaster, AppLanguageId> {
}
