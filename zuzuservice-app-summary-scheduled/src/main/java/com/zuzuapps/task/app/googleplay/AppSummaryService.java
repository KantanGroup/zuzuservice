package com.zuzuapps.task.app.googleplay;

import com.zuzuapps.task.app.common.*;
import com.zuzuapps.task.app.exceptions.ExceptionCodes;
import com.zuzuapps.task.app.exceptions.GooglePlayRuntimeException;
import com.zuzuapps.task.app.googleplay.models.SummaryApplicationPlays;
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
public class AppSummaryService extends AppCommonService {
    final Log logger = LogFactory.getLog("AppSummaryService");

    public void generateAppSummaryStore() {
        logger.info("[Application Summary Generation]Task start at: " + new Date());
        String dirPath = CommonUtils.folderBy(rootPath, DataServiceEnum.summary.name(), DataTypeEnum.generate.name()).getAbsolutePath();
        File dir = new File(dirPath);
        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            for (CollectionEnum collection : CollectionEnum.values()) {
                CommonUtils.createFile(Paths.get(dirPath, collection.name() + REGEX_3_UNDER_LINE + CategoryEnum.ALL.name().toLowerCase() + JSON_FILE_EXTENSION));
                for (CategoryEnum category : CategoryEnum.values()) {
                    CommonUtils.createFile(Paths.get(dirPath, collection.name() + REGEX_3_UNDER_LINE + category.name().toLowerCase() + JSON_FILE_EXTENSION));
                }
            }
        }
        logger.info("[Application Summary Generation]Task end at: " + new Date());
    }

    public void processAppSummaryStoreData() {
        while (true) {
            // something that should execute on weekdays only
            String dirPath = CommonUtils.folderBy(rootPath, DataServiceEnum.summary.name(), DataTypeEnum.generate.name()).getAbsolutePath();
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
                CollectionEnum collection = CollectionEnum.valueOf(data[0]);
                CategoryEnum category = CategoryEnum.valueOf(data[1].toUpperCase());
                int page = 1;
                while (true) {
                    long startTime = System.currentTimeMillis();
                    try {
                        SummaryApplicationPlays summaryApplicationPlays
                                = summaryApplicationPlayService.getSummaryApplications(category, collection, LANGUAGE_CODE_DEFAULT, COUNTRY_CODE_DEFAULT, page);
                        StringBuilder path = queueAppSummaryJSONPath(CommonUtils.getDailyByTime(), collection, category, page);
                        logger.debug("[Application Summary Store]Write summary of app to json " + path.toString());
                        Files.write(Paths.get(path.toString()), mapper.writeValueAsBytes(summaryApplicationPlays));
                        if (summaryApplicationPlays.getResults().size() < 120) {
                            break;
                        }
                    } catch (GooglePlayRuntimeException ex) {
                        if (ex.getCode() == ExceptionCodes.UNKNOWN_EXCEPTION) {
                            logger.error("[Application Summary Store][" + category.name() + "][" + collection.name() + "]Error " + ex.getMessage());
                        } else {
                            logger.info("[Application Summary Store][" + category.name() + "][" + collection.name() + "]Error " + ex.getMessage());
                        }
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

    private StringBuilder queueAppSummaryJSONPath(String time, CollectionEnum collection, CategoryEnum category, int page) {
        StringBuilder path = new StringBuilder(CommonUtils.folderBy(rootPath, DataServiceEnum.summary.name(), DataTypeEnum.queue.name()).getAbsolutePath());
        path.append("/");
        path.append(COUNTRY_CODE_DEFAULT).append(REGEX_3_UNDER_LINE);
        path.append(LANGUAGE_CODE_DEFAULT).append(REGEX_3_UNDER_LINE);
        path.append(category.name().toLowerCase()).append(REGEX_3_UNDER_LINE);
        path.append(collection.name().toLowerCase()).append(REGEX_3_UNDER_LINE);
        path.append(page).append(JSON_FILE_EXTENSION);
        return path;
    }

    /**
     * Daily app summary update
     */
    public void dailyAppSummaryUpdate() {
        while (true) {
            // something that should execute on weekdays only
            String dirPath = CommonUtils.folderBy(rootPath, DataServiceEnum.summary.name(), DataTypeEnum.queue.name()).getAbsolutePath();
            File dir = new File(dirPath);
            File[] files = dir.listFiles();
            if (files != null && files.length != 0) {
                try {
                    CommonUtils.sortFilesOrderByTime(files);
                    processAppSummary(files);
                } catch (Exception ex) {
                    logger.error("[ProcessError]Error " + ex.getMessage(), ex);
                }
            }
            CommonUtils.delay(timeWaitRuntimeLocal);
        }
    }

    private void processAppSummary(File[] files) throws Exception {
        logger.debug("[Application Summary --> Information]Cronjob end at: " + new Date());
        // something that should execute on weekdays only
        String time = CommonUtils.getDailyByTime();
        for (File json : files) {
            logger.info("[Application Summary --> Information]File " + json.getAbsolutePath());
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
