package com.jtests.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jtests.api.models.KanjiRadical;
import com.jtests.api.repositories.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author tuanta17
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestoreDatabaseTest {
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

    static String readFile(String path, Charset encoding)
            throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    @Test
    public void testBackupDatabase() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        /*
        List<Kanji> kanjis = mapper.readValue(readFile(PATH + "kanji.json", StandardCharsets.UTF_8), new TypeReference<List<Kanji>>() {});
        List<Radical> radicals = mapper.readValue(readFile(PATH + "radical.json", StandardCharsets.UTF_8), new TypeReference<List<Radical>>() {});
        List<Word> words = mapper.readValue(readFile(PATH + "word.json", StandardCharsets.UTF_8), new TypeReference<List<Word>>() {});
        List<Sentence> sentences = mapper.readValue(readFile(PATH + "sentence.json", StandardCharsets.UTF_8), new TypeReference<List<Sentence>>() {});
        List<Name> names = mapper.readValue(readFile(PATH + "name.json", StandardCharsets.UTF_8), new TypeReference<List<Name>>() {});
        kanjiRepository.save(kanjis);
        radicalRepository.save(radicals);
        wordRepository.save(words);
        sentenceRepository.save(sentences);
        nameRepository.save(names);
        */

        List<KanjiRadical> kanjiRadicals = mapper.readValue(readFile(PATH + "kanjiradical.json", StandardCharsets.UTF_8), new TypeReference<List<KanjiRadical>>() {
        });
        kanjiRadicalRepository.save(kanjiRadicals);
    }
}
