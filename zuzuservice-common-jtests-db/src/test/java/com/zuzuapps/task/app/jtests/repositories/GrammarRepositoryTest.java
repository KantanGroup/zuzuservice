package com.zuzuapps.task.app.jtests.repositories;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @author tuanta17
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GrammarRepositoryTest {
    @Autowired
    GrammarRepository repository;

    @Test
    public void testGetAllKanji() throws Exception {
        final ObjectMapper mapper = new ObjectMapper();
        Files.write(Paths.get("/tmp/grammars.json"), mapper.writeValueAsString(repository.findAllByOrderById().subList(0,488)).getBytes(), StandardOpenOption.CREATE);
    }
}
