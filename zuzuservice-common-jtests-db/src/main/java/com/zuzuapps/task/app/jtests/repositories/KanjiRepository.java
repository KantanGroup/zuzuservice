package com.zuzuapps.task.app.jtests.repositories;

import com.zuzuapps.task.app.jtests.models.Kanji;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author tuanta17
 */
public interface KanjiRepository extends PagingAndSortingRepository<Kanji, Integer> {

    List<Kanji> findAllByOrderByCodeAsc();

    List<Kanji> findByJlptLevelGreaterThan(@Param("level") int level);

    List<Kanji> findByGradeLevelGreaterThan(@Param("level") int level);

    List<Kanji> findByGradeLevel(@Param("level") int level);

    List<Kanji> findByJlptLevel(@Param("level") int level);
}
