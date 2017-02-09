package com.jtests.api.repositories;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jtests.api.models.Kanji;
import com.jtests.api.models.Radical;
import com.jtests.api.models.Sentence;
import com.jtests.api.models.Word;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by tuanta on 11/19/16.
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
}
