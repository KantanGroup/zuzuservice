package com.zuzuapps.task.app.googleplay;

import com.zuzuapps.task.app.common.*;
import com.zuzuapps.task.app.googleplay.models.SummaryApplicationPlays;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

/**
 * @author tuanta17
 */
@Service
public class AppSummaryService extends AppCommonService {
    final Log logger = LogFactory.getLog("AppSummaryService");

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
                        logger.error("[Application Summary Store][" + category.name() + "][" + collection.name() + "]Error " + ex.getMessage(), ex);
                        break;
                    }
                    page++;
                    CommonUtils.delay(timeGetAppSummary);
                }
            }
        }
        logger.info("[Application Summary Store]Cronjob end at: " + new Date());
    }

    private StringBuilder queueAppSummaryJSONPath(String time, CollectionEnum collection, CategoryEnum category, int page) {
        StringBuilder path = new StringBuilder(CommonUtils.folderBy(rootPath, DataServiceEnum.summary.name(), DataTypeEnum.queue.name()).getAbsolutePath());
        path.append("/");
        path.append(COUNTRY_CODE_DEFAULT).append(REGEX_3_UNDER_LINE);
        path.append(LANGUAGE_CODE_DEFAULT).append(REGEX_3_UNDER_LINE);
        path.append(category.name().toLowerCase()).append(REGEX_3_UNDER_LINE);
        path.append(collection.name().toLowerCase()).append(REGEX_3_UNDER_LINE);
        path.append(time).append(REGEX_3_UNDER_LINE);
        path.append(page).append(JSON_FILE_EXTENSTION);
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
            CommonUtils.delay(timeWaitRuntimeLocal);
        }
    }

    public void processAppSummary(File[] files) {
        logger.debug("[Application Summary --> Information]Cronjob end at: " + new Date());
        // something that should execute on weekdays only
        String time = CommonUtils.getDailyByTime();
        for (File json : files) {
            logger.debug("[Application Summary --> Information]File " + json.getAbsolutePath());
            String filename = json.getName();
            String[] data = filename.split(REGEX_3_UNDER_LINE);
            if (data.length >= 4) {
                String countryCode = data[0];
                String languageCode = data[1];
                CategoryEnum category = CategoryEnum.valueOf(data[2].toUpperCase());
                CollectionEnum collection = CollectionEnum.valueOf(data[3]);
                try {
                    SummaryApplicationPlays apps = mapper.readValue(json, SummaryApplicationPlays.class);
                    // Create app info json
                    queueAppInformation(apps.getResults(), countryCode, languageCode);
                    // Move data to log folder
                    moveFile(json.getAbsolutePath(), CommonUtils.folderBy(rootPath, DataServiceEnum.summary.name(), DataTypeEnum.log.name(), time, countryCode).getAbsolutePath());
                } catch (Exception ex) {
                    logger.error("[Application Summary --> Information][" + category.name() + "][" + collection.name() + "]Error " + ex.getMessage(), ex);
                    moveFile(json.getAbsolutePath(), CommonUtils.folderBy(rootPath, DataServiceEnum.summary.name(), DataTypeEnum.error.name(), time).getAbsolutePath());
                }
            } else {
                moveFile(json.getAbsolutePath(), CommonUtils.folderBy(rootPath, DataServiceEnum.summary.name(), DataTypeEnum.error.name(), time).getAbsolutePath());
            }
            CommonUtils.delay(5);
        }
        logger.debug("[Application Summary --> Information]Cronjob end at: " + new Date());
    }
}
