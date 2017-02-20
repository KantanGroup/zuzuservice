package com.zuzuapps.task.app.master.repositories;

import com.zuzuapps.task.app.master.models.CountryMaster;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author tuanta17
 */
public interface CountryMasterRepository extends CrudRepository<CountryMaster, Long> {
    public List<CountryMaster> findAllByTypeGreaterThanOrderByTypeDesc(int type);
}