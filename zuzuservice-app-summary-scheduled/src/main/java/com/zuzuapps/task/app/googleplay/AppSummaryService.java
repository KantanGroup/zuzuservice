package com.zuzuapps.task.app.googleplay;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zuzuapps.task.app.common.CategoryEnum;
import com.zuzuapps.task.app.common.CollectionEnum;
import com.zuzuapps.task.app.common.CommonUtils;
import com.zuzuapps.task.app.googleplay.models.SummaryApplicationPlays;
import com.zuzuapps.task.app.googleplay.servies.SummaryApplicationPlayService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

/**
 * @author tuanta17
 */
@Service
public class AppSummaryService {
    public static final String COUNTRY_CODE_DEFAULT = "us";
    public static final String LANGUAGE_CODE_DEFAULT = "en";
    private final ObjectMapper mapper = new ObjectMapper();
    private final Log logger = LogFactory.getLog(ScheduleApplication.class);

    @Value("${data.root.path:/tmp}")
    private String rootPath;
    @Value("${time.get.app.info:5000}")
    private long timeGetAppInfo;
    @Autowired
    private SummaryApplicationPlayService summaryApplicationPlayService;

    /**
     * Get all in USA
     */
    public void appSummary() {
        logger.info("[Application Summary Store]Cronjob start at: " + new Date());
        // something that should execute on weekdays only
        String time = CommonUtils.getDailyByTime();
        for (CollectionEnum collection : CollectionEnum.values()) {
            for (CategoryEnum category : CategoryEnum.values()) {
                int page = 1;
                while (true) {
                    try {
                        SummaryApplicationPlays summaryApplicationPlays
                                = summaryApplicationPlayService.getSummaryApplications(category, collection, LANGUAGE_CODE_DEFAULT, COUNTRY_CODE_DEFAULT, page);
                        StringBuilder path = queueAppSummaryJSONPath(time, collection, category, page);
                        logger.debug("[Application Summary Store]Write app summary to json " + path.toString());
                        Files.write(Paths.get(path.toString()), mapper.writeValueAsBytes(summaryApplicationPlays));
                        if (summaryApplicationPlays.getResults().size() < 120) {
                            break;
                        }
                    } catch (Exception ex) {
                        logger.error("[Application Summary Store]App summary error", ex);
                        break;
                    }
                    page++;
                    CommonUtils.delay(timeGetAppInfo);
                }
            }
        }
        logger.info("[Application Summary Store]Cronjob end at: " + new Date());
    }

    private StringBuilder queueAppSummaryJSONPath(String time, CollectionEnum collection, CategoryEnum category, int page) {
        StringBuilder path = new StringBuilder(CommonUtils.queueSummaryFolderBy(rootPath, time));
        path.append("/").append(COUNTRY_CODE_DEFAULT).append("___");
        path.append(category.name().toLowerCase()).append("___");
        path.append(collection.name().toLowerCase()).append("___");
        path.append(time).append("___").append(page).append(".json");
        return path;
    }

    public void dailyAppSummaryUpdate() {
        while (true) {
            String time = CommonUtils.getDailyByTime();
            // something that should execute on weekdays only
            String dirPath = CommonUtils.queueSummaryFolderBy(rootPath, time);
            File dir = new File(dirPath);
            File[] files = dir.listFiles();
            if (files != null && files.length != 0) {
                processAppSummary(files);
            }
            CommonUtils.delay(timeGetAppInfo);
        }
    }

    public void processAppSummary(File[] files) {
        logger.info("[Application Summary --> Information]Cronjob end at: " + new Date());
        for (File json : files) {

        }
        logger.info("[Application Summary --> Information]Cronjob end at: " + new Date());
    }
}
