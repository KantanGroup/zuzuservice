package com.zuzuapps.task.app.googleplay;

import com.zuzuapps.task.app.common.*;
import com.zuzuapps.task.app.googleplay.models.SummaryApplicationPlays;
import com.zuzuapps.task.app.master.models.AppIndexMaster;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author tuanta17
 */
@Service
public class AppSummaryService extends AppCommonService {
    /**
     * Get all in USA
     */
    public void appSummary() {
        logger.info("[Application Summary Store]Cronjob start at: " + new Date());
        // something that should execute on weekdays only
        for (CollectionEnum collection : CollectionEnum.values()) {
            for (CategoryEnum category : CategoryEnum.values()) {
                int page = 1;
                while (true) {
                    try {
                        SummaryApplicationPlays summaryApplicationPlays
                                = summaryApplicationPlayService.getSummaryApplications(category, collection, LANGUAGE_CODE_DEFAULT, COUNTRY_CODE_DEFAULT, page);
                        StringBuilder path = queueAppSummaryJSONPath(CommonUtils.getDailyByTime(), collection, category, page);
                        logger.debug("[Application Summary Store]Write summary of app to json " + path.toString());
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
        StringBuilder path = new StringBuilder(CommonUtils.folderBy(rootPath, DataServiceEnum.summary.name(), DataTypeEnum.queue.name()).getAbsolutePath());
        path.append("/");
        path.append(COUNTRY_CODE_DEFAULT).append("___");
        path.append(LANGUAGE_CODE_DEFAULT).append("___");
        path.append(category.name().toLowerCase()).append("___");
        path.append(collection.name().toLowerCase()).append("___");
        path.append(time).append("___");
        path.append(page).append(".json");
        return path;
    }

    public void dailyAppSummaryUpdate() {
        while (true) {
            // something that should execute on weekdays only
            String dirPath = CommonUtils.folderBy(rootPath, DataServiceEnum.summary.name(), DataTypeEnum.queue.name()).getAbsolutePath();
            File dir = new File(dirPath);
            File[] files = dir.listFiles();
            if (files != null && files.length != 0) {
                processAppSummary(files);
            }
            CommonUtils.delay(timeGetAppInfo);
        }
    }

    public void processAppSummary(File[] files) {
        logger.info("[Application Summary]Cronjob end at: " + new Date());
        // something that should execute on weekdays only
        String time = CommonUtils.getDailyByTime();
        for (File json : files) {
            try {
                logger.debug("[Application Summary]File " + json.getAbsolutePath());
                List<AppIndexMaster> appIndexMasters = new ArrayList<AppIndexMaster>();
                String filename = json.getName();
                String[] data = filename.split("___");
                String countryCode = data[0];
                String languageCode = data[1];
                CategoryEnum category = CategoryEnum.valueOf(data[2].toUpperCase());
                CollectionEnum collection = CollectionEnum.valueOf(data[3]);
                String fileTime = data[4];
                Date fileDateTime = new Date(Long.valueOf(fileTime));
                SummaryApplicationPlays apps = mapper.readValue(json, SummaryApplicationPlays.class);
                // Create app info json
                queueAppInformation(apps.getResults(), countryCode, languageCode);
                // Move data to log folder
                moveFile(json.getAbsolutePath(), CommonUtils.folderBy(rootPath, DataServiceEnum.summary.name(), DataTypeEnum.log.name(), time, countryCode).getAbsolutePath());
            } catch (Exception ex) {
                logger.error("[Application Summary]Write summary of app error", ex);
                moveFile(json.getAbsolutePath(), CommonUtils.folderBy(rootPath, DataServiceEnum.summary.name(), DataTypeEnum.error.name(), time).getAbsolutePath());
            }
            CommonUtils.delay(5);
        }
        logger.info("[Application Summary]Cronjob end at: " + new Date());
    }
}
