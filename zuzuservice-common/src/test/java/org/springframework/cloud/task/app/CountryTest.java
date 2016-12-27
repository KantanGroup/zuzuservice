package org.springframework.cloud.task.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.cloud.task.app.local.models.CountryLocal;
import org.springframework.cloud.task.app.local.repositories.CountryRepository;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author tuanta17
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(GooglePlayCommonConfiguration.class)
public class CountryTest {
    @Autowired
    CountryRepository countryRepository;

    @Test
    public void testAddCountryData() throws Exception {
        File countryFile = Paths.get("src", "test", "resources", "country_code_language_code.csv").toFile();
        BufferedReader in = new BufferedReader(new FileReader(countryFile));
        String str;

        List<String[]> codes = new ArrayList<String[]>();
        List<CountryLocal> locals = new ArrayList<>();
        while ((str = in.readLine()) != null) {
            String[] code = str.split(",");
            if (code.length == 4) {
                CountryLocal countryLocal = new CountryLocal();
                countryLocal.setCountryName(code[0]);
                countryLocal.setCountryCode(code[1]);
                countryLocal.setLanguageName(code[2]);
                countryLocal.setLanguageCode(code[3]);
                countryLocal.setCreateAt(new Date());
                locals.add(countryLocal);
            }
        }
        in.close();

        // countryRepository.save(locals);
        final ObjectMapper mapper = new ObjectMapper();
        Files.write(Paths.get("/Users/tuan/countries.json"), mapper.writeValueAsString(locals).getBytes(), StandardOpenOption.CREATE);
    }
}


