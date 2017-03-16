package com.zuzuapps.task.app.repositories;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zuzuapps.task.app.common.CommonUtils;
import com.zuzuapps.task.app.solr.googlestore.models.GoogleAppInformationSolr;
import com.zuzuapps.task.app.solr.googlestore.repositories.GoogleAppInformationSolrRepository;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author tuanta17
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ImportDataToSolrTest {
    static final String root = "/opt/zuzuservice/queue/";

    @Autowired
    GoogleAppInformationSolrRepository appInformationSolrRepository;

    @Test
    public void exportAppIndex() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        while (true) {
            File dir = Paths.get(root).toFile();
            File[] files = dir.listFiles();
            if (files != null && files.length != 0) {
                for (File json : files) {
                    try {
                        List<GoogleAppInformationSolr> apps = mapper.readValue(json, new TypeReference<List<GoogleAppInformationSolr>>(){});
                        appInformationSolrRepository.save(apps);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    FileUtils.deleteQuietly(json);
                    CommonUtils.delay(100);
                }
            }
            CommonUtils.delay(100);
        }
    }
}
