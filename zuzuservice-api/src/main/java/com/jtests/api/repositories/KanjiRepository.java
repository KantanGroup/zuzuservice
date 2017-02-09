package com.jtests.api.repositories;

import com.jtests.api.models.Kanji;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

/**
 * @author tuanta17
 */
//@RepositoryRestResource(path = "kanjis")
public interface KanjiRepository extends PagingAndSortingRepository<Kanji, Integer> {

    @RestResource(path = "allKanji", rel = "allKanji")
    List<Kanji> findAllByOrderByCodeAsc();

    @RestResource(path = "jlptKanji", rel = "jlptKanji")
    List<Kanji> findByJlptLevelGreaterThan(@Param("level") int level);

    @RestResource(path = "kanjiOrderBy", rel = "kanjiOrderBy")
    Page<Kanji> findAllByOrderByJlptLevelDescGradeLevelAscFrequencyAsc(Pageable pageable);

    @RestResource(path = "jlpt", rel = "jlpt")
    Page<Kanji> findByJlptLevel(@Param("jlpt") int jlptLevel, Pageable pageable);

    @RestResource(path = "grade", rel = "grade")
    Page<Kanji> findByGradeLevel(@Param("grade") int gradeLevel, Pageable pageable);
}
