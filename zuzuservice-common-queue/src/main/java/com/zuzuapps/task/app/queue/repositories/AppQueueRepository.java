package com.zuzuapps.task.app.queue.repositories;

import com.zuzuapps.task.app.queue.models.AppQueue;
import org.springframework.data.repository.CrudRepository;

/**
 * @author tuanta17
 */
public interface AppQueueRepository extends CrudRepository<AppQueue, String> {
}
