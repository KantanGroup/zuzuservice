package com.zuzuapps.task.app.jtests.repositories;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zuzuapps.task.app.jtests.models.KanjiRadical;
import com.zuzuapps.task.app.jtests.models.KanjiRadicalId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author tuanta17
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class KanjiRadicalRepositoryTest {
    @Autowired
    KanjiRadicalRepository repository;

    @Test
    public void testGetAllKanji() throws Exception {
        System.out.println(repository.count());
        ObjectMapper mapper = new ObjectMapper();
        KanjiRadicalId id = new KanjiRadicalId();
        id.setKanjiCode(19968);
        id.setRadicalCode(19968);
        KanjiRadical kanji = repository.findOne(id);
        System.out.println(mapper.writeValueAsString(kanji));
    }
}
