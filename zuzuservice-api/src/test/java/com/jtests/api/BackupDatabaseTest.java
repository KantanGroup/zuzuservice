package com.jtests.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jtests.api.repositories.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;

/**
 * @author tuanta17
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BackupDatabaseTest {
    static final String PATH = "/home/tuanta/Documents/gcloud-server/kanjistudy/";
    @Autowired
    KanjiRepository kanjiRepository;
    @Autowired
    RadicalRepository radicalRepository;
    @Autowired
    WordRepository wordRepository;
    @Autowired
    SentenceRepository sentenceRepository;
    @Autowired
    NameRepository nameRepository;
    @Autowired
    KanjiRadicalRepository kanjiRadicalRepository;

    @Test
    public void testBackupDatabase() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        /*
        mapper.writeValue(new File(PATH + "kanji.json"), kanjiRepository.findAll());
        mapper.writeValue(new File(PATH + "radical.json"), radicalRepository.findAll());
        mapper.writeValue(new File(PATH + "word.json"), wordRepository.findAll());
        mapper.writeValue(new File(PATH + "sentence.json"), sentenceRepository.findAll());
        mapper.writeValue(new File(PATH + "name.json"), nameRepository.findAll());
        */
        mapper.writeValue(new File(PATH + "kanjiradical.json"), kanjiRadicalRepository.findAll());
    }
}
