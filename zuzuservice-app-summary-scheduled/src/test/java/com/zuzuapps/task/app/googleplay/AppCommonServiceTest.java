package com.zuzuapps.task.app.googleplay;

import com.zuzuapps.task.app.GooglePlayCommonConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.file.Paths;

/**
 * @author tuanta17
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({GooglePlayCommonConfiguration.class})
public class AppCommonServiceTest {
    @Autowired
    AppCommonService appCommonService;

    @Test
    public void zipFile() {
        appCommonService.moveFile(Paths.get("src", "test", "resources", "test.gzip.json").toFile().getAbsolutePath(), Paths.get("/tmp").toFile().getAbsolutePath());
        appCommonService.moveFile(Paths.get("src", "test", "resources", "app.summary.json").toFile().getAbsolutePath(), Paths.get("/tmp").toFile().getAbsolutePath());
    }
}
