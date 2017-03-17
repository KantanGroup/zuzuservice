package com.zuzuapps.task.app.appstore;

import com.zuzuapps.task.app.appstore.common.AppleCategoryEnum;
import com.zuzuapps.task.app.appstore.common.AppleCollectionEnum;
import com.zuzuapps.task.app.appstore.models.CountryMaster;
import com.zuzuapps.task.app.appstore.models.SummaryApplicationAppStores;
import com.zuzuapps.task.app.common.CommonUtils;
import com.zuzuapps.task.app.common.DataServiceEnum;
import com.zuzuapps.task.app.common.DataTypeEnum;
import com.zuzuapps.task.app.exceptions.AppStoreRuntimeException;
import com.zuzuapps.task.app.exceptions.ExceptionCodes;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

/**
 * @author tuanta17
 */
@Service
public class AppleAppIndexService extends AppleAppCommonService {
    final Log logger = LogFactory.getLog("AppleAppIndexService");

    public void generateAppIndexStore() {
        logger.info("[AppleAppIndexService][Index Generation]Task start at: " + new Date());
        String time = CommonUtils.getDailyByTime();
        String dirPath = CommonUtils.folderBy(appleRootPath, DataServiceEnum.top_app_daily.name(), DataTypeEnum.generate.name(), time).getAbsolutePath();
        File dir = new File(dirPath);
        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            List<CountryMaster> countries = getCountries();
            for (CountryMaster countryMaster : countries) {
                for (AppleCollectionEnum collection : AppleCollectionEnum.values()) {
                    CommonUtils.createFile(Paths.get(dirPath, countryMaster.getCountryCode() + REGEX_3_UNDER_LINE + countryMaster.getLanguageCode() + REGEX_3_UNDER_LINE + AppleCategoryEnum.ALL.getCategory() + REGEX_3_UNDER_LINE + time + JSON_FILE_EXTENSION));
                    for (AppleCategoryEnum category : AppleCategoryEnum.values()) {
                        CommonUtils.createFile(Paths.get(dirPath, countryMaster.getCountryCode() + REGEX_3_UNDER_LINE + countryMaster.getLanguageCode() + REGEX_3_UNDER_LINE + collection.getCollection() + REGEX_3_UNDER_LINE + category.getCategory() + REGEX_3_UNDER_LINE + time + JSON_FILE_EXTENSION));
                    }
                }
            }
        }
        logger.info("[AppleAppIndexService][Index Generation]Task end at: " + new Date());
    }

    /**
     * Write app index of category in to json
     */
    public void processAppIndexStoreData() {
        while (true) {
            // something that should execute on weekdays only
            String time = CommonUtils.getDailyByTime();
            String dirPath = CommonUtils.folderBy(appleRootPath, DataServiceEnum.top_app_daily.name(), DataTypeEnum.generate.name(), time).getAbsolutePath();
            File dir = new File(dirPath);
            File[] files = dir.listFiles();
            if (files != null && files.length != 0) {
                try {
                    CommonUtils.sortFilesOrderByTime(files);
                    processAppIndexStoreData(files);
                } catch (Exception ex) {
                    logger.error("[ProcessError]Error " + ex.getMessage(), ex);
                }
            }
            CommonUtils.delay(timeWaitRuntimeLocal);
        }
    }

    private void processAppIndexStoreData(File[] files) throws Exception {
        logger.info("[AppleAppIndexService][Application Index]Task start at: " + new Date());
        // something that should execute on weekdays only
        String time = CommonUtils.getDailyByTime();
        for (File json : files) {
            logger.info("[AppleAppIndexService][Application Index]File " + json.getAbsolutePath());
            String filename = json.getName();
            String[] data = filename.split(REGEX_3_UNDER_LINE);
            if (data.length >= 4) {
                String countryCode = data[0];
                String languageCode = data[1];
                AppleCollectionEnum collection = AppleCollectionEnum.findByKey(data[2]);
                AppleCategoryEnum category = AppleCategoryEnum.findByKey(data[3]);
                long startTime = System.currentTimeMillis();
                try {
                    SummaryApplicationAppStores summaryApplicationPlays
                            = summaryApplicationAppStoreService.getSummaryApplications(category, collection, countryCode, 0);
                    StringBuilder path = queueAppIndexJSONPath(time, countryCode, languageCode, collection, category);
                    logger.debug("[AppleAppIndexService][Application Index]Write app summary to json " + path.toString());
                    Files.write(Paths.get(path.toString()), mapper.writeValueAsBytes(summaryApplicationPlays));
                    logger.debug("[AppleAppIndexService][Application Index]Delete file " + json.getAbsolutePath());
                    FileUtils.deleteQuietly(json);
                } catch (AppStoreRuntimeException ex) {
                    if (ex.getCode() == ExceptionCodes.NETWORK_LIMITED_EXCEPTION) {
                        logger.info("[AppleAppIndexService][Application Index][" + category.name() + "][" + collection.name() + "]Error " + ex.getMessage());
                    } else {
                        FileUtils.deleteQuietly(json);
                        if (ex.getCode() == ExceptionCodes.UNKNOWN_EXCEPTION) {
                            logger.error("[AppleAppIndexService][Application Index][" + category.name() + "][" + collection.name() + "]Error " + ex.getMessage(), ex);
                        } else {
                            logger.info("[AppleAppIndexService][Application Index][" + category.name() + "][" + collection.name() + "]Error " + ex.getMessage());
                        }
                    }
                } catch (Exception ex) {
                    logger.error("[AppleAppIndexService][Application Index][" + countryCode + "][" + category.name() + "][" + collection.name() + "]Error " + ex.getMessage(), ex);
                    FileUtils.deleteQuietly(json);
                }
                long delayTime = System.currentTimeMillis() - startTime;
                CommonUtils.delay(timeGetAppSummary - delayTime);
            } else {
                FileUtils.deleteQuietly(json);
            }
            CommonUtils.delay(5);
        }
        logger.info("[AppleAppIndexService][Application Index]Task end at: " + new Date());
    }


    private StringBuilder queueAppIndexJSONPath(String time, String countryCode, String languageCode, AppleCollectionEnum collection, AppleCategoryEnum category) {
        StringBuilder path = new StringBuilder(CommonUtils.folderBy(appleRootPath, DataServiceEnum.top_app_daily.name(), DataTypeEnum.queue.name(), time).getAbsolutePath());
        path.append("/");
        path.append(countryCode).append(REGEX_3_UNDER_LINE);
        path.append(languageCode).append(REGEX_3_UNDER_LINE);
        path.append(category.getCategory()).append(REGEX_3_UNDER_LINE);
        path.append(collection.getCollection()).append(REGEX_3_UNDER_LINE);
        path.append(time).append(REGEX_3_UNDER_LINE);
        path.append(ZERO_NUMBER).append(JSON_FILE_EXTENSION);
        return path;
    }
}
