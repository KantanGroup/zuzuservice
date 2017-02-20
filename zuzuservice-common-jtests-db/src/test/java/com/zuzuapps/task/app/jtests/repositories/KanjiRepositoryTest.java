package com.zuzuapps.task.app.jtests.repositories;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zuzuapps.task.app.jtests.models.Kanji;
import com.zuzuapps.task.app.jtests.models.Radical;
import com.zuzuapps.task.app.jtests.models.Sentence;
import com.zuzuapps.task.app.jtests.models.Word;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/**
 * @author tuanta17
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class KanjiRepositoryTest {
    @Autowired
    KanjiRepository repository;

    @Test
    public void testGetAllKanji() throws Exception {
        System.out.println(repository.count());
        ObjectMapper mapper = new ObjectMapper();
        Kanji kanji = repository.findOne(19971);
        System.out.println(kanji.getRadicals().size());
        for (Radical radical : kanji.getRadicals()) {
            System.out.println(radical.getCode());
        }
        System.out.println(kanji.getWords().size());
        for (Word word : kanji.getWords()) {
            System.out.println(word.getWord());
        }
        System.out.println(kanji.getSentences().size());
        for (Sentence sentence : kanji.getSentences()) {
            System.out.println(sentence.getSentence());
        }
    }

    @Test
    public void testAllJLPTKanji() throws Exception {
        List<Kanji> kanjis = repository.findAllByOrderByCodeAsc();
        System.out.println(repository.count());
        List<Kanji> filterKanjis = new ArrayList<>();
        for (Kanji kanji : kanjis) {
            if (kanji.getJlptLevel() > 0 || kanji.getGradeLevel() > 0)
                filterKanjis.add(kanji);
        }
        System.out.println(filterKanjis.size());
        final ObjectMapper mapper = new ObjectMapper();
        Files.write(Paths.get("/tmp/kanjis.json"), mapper.writeValueAsString(filterKanjis).getBytes(), StandardOpenOption.CREATE);
    }
}
