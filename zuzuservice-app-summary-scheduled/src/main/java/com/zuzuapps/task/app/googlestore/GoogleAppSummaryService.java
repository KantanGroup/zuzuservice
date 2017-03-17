package com.zuzuapps.task.app.googlestore;

import com.zuzuapps.task.app.common.*;
import com.zuzuapps.task.app.exceptions.GooglePlayRuntimeException;
import com.zuzuapps.task.app.googlestore.common.GoogleCollectionEnum;
import com.zuzuapps.task.app.googlestore.common.GoogleCategoryEnum;
import com.zuzuapps.task.app.googlestore.models.SummaryApplicationGooglePlays;
import org.apache.commons.io.FileUtils;
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
public class GoogleAppSummaryService extends GoogleAppCommonService {
    final Log logger = LogFactory.getLog("GoogleAppSummaryService");

    public void generateAppSummaryStore() {
        logger.info("[Application Summary Generation]Task start at: " + new Date());
        String dirPath = CommonUtils.folderBy(googleRootPath, DataServiceEnum.top_app_summary.name(), DataTypeEnum.generate.name()).getAbsolutePath();
        File dir = new File(dirPath);
        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            for (GoogleCollectionEnum collection : GoogleCollectionEnum.values()) {
                CommonUtils.createFile(Paths.get(dirPath, collection.name() + REGEX_3_UNDER_LINE + GoogleCategoryEnum.ALL.name().toLowerCase() + JSON_FILE_EXTENSION));
                for (GoogleCategoryEnum category : GoogleCategoryEnum.values()) {
                    CommonUtils.createFile(Paths.get(dirPath, collection.name() + REGEX_3_UNDER_LINE + category.name().toLowerCase() + JSON_FILE_EXTENSION));
                }
            }
        }
        logger.info("[Application Summary Generation]Task end at: " + new Date());
    }

    public void processAppSummaryStoreData() {
        while (true) {
            // something that should execute on weekdays only
            String dirPath = CommonUtils.folderBy(googleRootPath, DataServiceEnum.top_app_summary.name(), DataTypeEnum.generate.name()).getAbsolutePath();
            File dir = new File(dirPath);
            File[] files = dir.listFiles();
            if (files != null && files.length != 0) {
                try {
                    CommonUtils.sortFilesOrderByTime(files);
                    processAppSummaryStore(files);
                } catch (Exception ex) {
                    logger.error("[ProcessError]Error " + ex.getMessage(), ex);
                }
            }
            CommonUtils.delay(timeWaitRuntimeLocal);
        }
    }

    /**
     * Get all in USA
     */
    private void processAppSummaryStore(File[] files) throws Exception {
        logger.info("[Application Summary Store]Cronjob start at: " + new Date());
        // something that should execute on weekdays only
        for (File json : files) {
            logger.info("[Application Summary Store]File " + json.getAbsolutePath());
            String filename = json.getName();
            String[] data = filename.split(REGEX_3_UNDER_LINE);
            if (data.length >= 2) {
                GoogleCollectionEnum collection = GoogleCollectionEnum.valueOf(data[0]);
                GoogleCategoryEnum category = GoogleCategoryEnum.valueOf(data[1].replaceAll(JSON_FILE_EXTENSION, "").toUpperCase());
                int page = 1;
                while (true) {
                    long startTime = System.currentTimeMillis();
                    try {
                        SummaryApplicationGooglePlays summaryApplicationPlays
                                = summaryApplicationPlayService.getSummaryApplications(category, collection, LANGUAGE_CODE_DEFAULT, COUNTRY_CODE_DEFAULT, page);
                        StringBuilder path = queueAppSummaryJSONPath(CommonUtils.getDailyByTime(), collection, category, page);
                        logger.debug("[Application Summary Store]Write summary of app to json " + path.toString());
                        Files.write(Paths.get(path.toString()), mapper.writeValueAsBytes(summaryApplicationPlays));
                        if (summaryApplicationPlays.getResults().size() < 120) {
                            break;
                        }
                    } catch (GooglePlayRuntimeException ex) {
                        logger.info("[Application Summary Store][" + category.name() + "][" + collection.name() + "]Error " + ex.getMessage());
                        break;
                    } catch (Exception ex) {
                        logger.error("[Application Summary Store][" + category.name() + "][" + collection.name() + "]Error " + ex.getMessage(), ex);
                        break;
                    }
                    page++;
                    long delayTime = System.currentTimeMillis() - startTime;
                    CommonUtils.delay(timeGetAppSummary - delayTime);
                }
            }
            FileUtils.deleteQuietly(json);
            CommonUtils.delay(5);
        }
        logger.info("[Application Summary Store]Cronjob end at: " + new Date());
    }

    private StringBuilder queueAppSummaryJSONPath(String time, GoogleCollectionEnum collection, GoogleCategoryEnum category, int page) {
        StringBuilder path = new StringBuilder(CommonUtils.folderBy(googleRootPath, DataServiceEnum.top_app_summary.name(), DataTypeEnum.queue.name()).getAbsolutePath());
        path.append("/");
        path.append(COUNTRY_CODE_DEFAULT).append(REGEX_3_UNDER_LINE);
        path.append(LANGUAGE_CODE_DEFAULT).append(REGEX_3_UNDER_LINE);
        path.append(category.name().toLowerCase()).append(REGEX_3_UNDER_LINE);
        path.append(collection.name().toLowerCase()).append(REGEX_3_UNDER_LINE);
        path.append(page).append(JSON_FILE_EXTENSION);
        return path;
    }
}
