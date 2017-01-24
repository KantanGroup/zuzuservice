package com.zuzuapps.task.app.googleplay;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * @author tuanta17
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AppCommonServiceTest {
    @Autowired
    AppCommonService appCommonService;

    @Test
    public void zipFile() throws IOException {
        Path appSummaryBak = Paths.get("src", "test", "resources", "app.summary.bak.json");
        Path appSummary = Paths.get("src", "test", "resources", "app.summary.json");
        Files.copy(appSummaryBak, appSummary, StandardCopyOption.REPLACE_EXISTING);
        appCommonService.moveFile(appSummary.toFile().getAbsolutePath(), Paths.get("/tmp").toFile().getAbsolutePath());
    }
}
