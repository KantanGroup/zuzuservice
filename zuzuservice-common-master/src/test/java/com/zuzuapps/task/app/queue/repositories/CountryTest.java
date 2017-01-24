package com.zuzuapps.task.app.queue.repositories;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zuzuapps.task.app.GooglePlayCommonConfiguration;
import com.zuzuapps.task.app.master.models.CountryMaster;
import com.zuzuapps.task.app.master.repositories.CountryMasterRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
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
@Import({GooglePlayCommonConfiguration.class})
public class CountryTest {
    @Autowired
    CountryMasterRepository countryRepository;

    @Test
    public void testAddCountryData() throws Exception {
        File countryFile = Paths.get("src", "test", "resources", "country_code_language_code.csv").toFile();
        BufferedReader in = new BufferedReader(new FileReader(countryFile));
        String str;

        List<String[]> codes = new ArrayList<String[]>();
        List<CountryMaster> locals = new ArrayList<>();
        while ((str = in.readLine()) != null) {
            String[] code = str.split(",");
            if (code.length == 4) {
                CountryMaster countryLocal = new CountryMaster();
                countryLocal.setCountryName(code[0]);
                countryLocal.setCountryCode(code[1]);
                countryLocal.setLanguageName(code[2]);
                countryLocal.setLanguageCode(code[3]);
                countryLocal.setCreateAt(new Date());
                locals.add(countryLocal);
            }
        }
        in.close();
        //CREATE SCHEMA `dataflows` DEFAULT CHARACTER SET utf8mb4 ;
        // countryRepository.save(locals);
        final ObjectMapper mapper = new ObjectMapper();
        Files.write(Paths.get("/tmp/countries.json"), mapper.writeValueAsString(locals).getBytes(), StandardOpenOption.CREATE);
    }


    @Test
    public void testGetOrderByTypeDesc() throws Exception {
        List<CountryMaster> countries = countryRepository.findAllByTypeGreaterThanOrderByTypeDesc(0);
        System.out.println(countries.size());
        for (CountryMaster countryMaster : countries) {
            System.out.println(countryMaster.getType() + " -- " + countryMaster.getCountryCode() + " -- " + countryMaster.getCountryName());
        }
    }
}


