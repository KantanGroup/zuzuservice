package com.zuzuapps.task.app.googlestore;

import com.zuzuapps.task.app.appstore.models.CountryMaster;
import com.zuzuapps.task.app.common.CommonUtils;
import com.zuzuapps.task.app.common.DataServiceEnum;
import com.zuzuapps.task.app.common.DataTypeEnum;
import com.zuzuapps.task.app.exceptions.ExceptionCodes;
import com.zuzuapps.task.app.exceptions.GooglePlayRuntimeException;
import com.zuzuapps.task.app.googlestore.common.GoogleCategoryEnum;
import com.zuzuapps.task.app.googlestore.common.GoogleCollectionEnum;
import com.zuzuapps.task.app.googlestore.models.SummaryApplicationGooglePlays;
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
public class GoogleAppIndexService extends GoogleAppCommonService {
    final Log logger = LogFactory.getLog("GoogleAppIndexService");

    public void generateAppIndexStore() {
        logger.info("[GoogleAppIndexService][Index Generation]Task start at: " + new Date());
        String time = CommonUtils.getDailyByTime();
        String dirPath = CommonUtils.folderBy(googleRootPath, DataServiceEnum.top_app_daily.name(), DataTypeEnum.generate.name(), time).getAbsolutePath();
        File dir = new File(dirPath);
        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            List<CountryMaster> countries = getCountries();
            for (CountryMaster countryMaster : countries) {
                for (GoogleCollectionEnum collection : GoogleCollectionEnum.values()) {
                    CommonUtils.createFile(Paths.get(dirPath, countryMaster.getCountryCode() + REGEX_3_UNDER_LINE + countryMaster.getLanguageCode() + REGEX_3_UNDER_LINE + collection.name() + REGEX_3_UNDER_LINE + GoogleCategoryEnum.ALL.name().toLowerCase() + REGEX_3_UNDER_LINE + time + JSON_FILE_EXTENSION));
                    for (GoogleCategoryEnum category : GoogleCategoryEnum.values()) {
                        CommonUtils.createFile(Paths.get(dirPath, countryMaster.getCountryCode() + REGEX_3_UNDER_LINE + countryMaster.getLanguageCode() + REGEX_3_UNDER_LINE + collection.name() + REGEX_3_UNDER_LINE + category.name().toLowerCase() + REGEX_3_UNDER_LINE + time + JSON_FILE_EXTENSION));
                    }
                }
            }
        }
        logger.info("[GoogleAppIndexService][Index Generation]Task end at: " + new Date());
    }

    /**
     * Write app index of category in to json
     */
    public void processAppIndexStoreData() {
        while (true) {
            // something that should execute on weekdays only
            String time = CommonUtils.getDailyByTime();
            String dirPath = CommonUtils.folderBy(googleRootPath, DataServiceEnum.top_app_daily.name(), DataTypeEnum.generate.name(), time).getAbsolutePath();
            File dir = new File(dirPath);
            File[] files = dir.listFiles();
            if (files != null && files.length != 0) {
                try {
                    CommonUtils.sortFilesOrderByTime(files);
                    processAppIndexStoreData(files);
                } catch (Exception ex) {
                    logger.error("[GoogleAppIndexService][ProcessError]Error " + ex.getMessage(), ex);
                }
            }
            CommonUtils.delay(timeWaitRuntimeLocal);
        }
    }

    private void processAppIndexStoreData(File[] files) throws Exception {
        logger.info("[GoogleAppIndexService][Application Index]Task start at: " + new Date());
        // something that should execute on weekdays only
        String time = CommonUtils.getDailyByTime();
        for (File json : files) {
            logger.info("[GoogleAppIndexService][Application Index]File " + json.getAbsolutePath());
            String filename = json.getName();
            String[] data = filename.split(REGEX_3_UNDER_LINE);
            if (data.length >= 4) {
                String countryCode = data[0];
                String languageCode = data[1];
                GoogleCollectionEnum collection = GoogleCollectionEnum.valueOf(data[2]);
                GoogleCategoryEnum category = GoogleCategoryEnum.valueOf(data[3].toUpperCase());
                long startTime = System.currentTimeMillis();
                try {
                    SummaryApplicationGooglePlays summaryApplicationPlays
                            = summaryApplicationPlayService.getSummaryApplications(category, collection, languageCode, countryCode, 0);
                    StringBuilder path = queueAppIndexJSONPath(time, countryCode, languageCode, collection, category);
                    logger.debug("[GoogleAppIndexService][Application Index]Write app summary to json " + path.toString());
                    Files.write(Paths.get(path.toString()), mapper.writeValueAsBytes(summaryApplicationPlays));
                    logger.debug("[GoogleAppIndexService][Application Index]Delete file " + json.getAbsolutePath());
                    FileUtils.deleteQuietly(json);
                } catch (GooglePlayRuntimeException ex) {
                    if (ex.getCode() == ExceptionCodes.NETWORK_LIMITED_EXCEPTION) {
                        logger.info("[GoogleAppIndexService][Application Index][" + category.name() + "][" + collection.name() + "]Error " + ex.getMessage());
                    } else {
                        FileUtils.deleteQuietly(json);
                        if (ex.getCode() == ExceptionCodes.UNKNOWN_EXCEPTION) {
                            logger.error("[GoogleAppIndexService][Application Index][" + category.name() + "][" + collection.name() + "]Error " + ex.getMessage(), ex);
                        } else {
                            logger.info("[GoogleAppIndexService][Application Index][" + category.name() + "][" + collection.name() + "]Error " + ex.getMessage());
                        }
                    }
                } catch (Exception ex) {
                    logger.error("[GoogleAppIndexService][Application Index][" + countryCode + "][" + category.name() + "][" + collection.name() + "]Error " + ex.getMessage(), ex);
                    FileUtils.deleteQuietly(json);
                }
                long delayTime = System.currentTimeMillis() - startTime;
                CommonUtils.delay(timeGetAppSummary - delayTime);
            } else {
                FileUtils.deleteQuietly(json);
            }
            CommonUtils.delay(5);
        }
        logger.info("[GoogleAppIndexService][Application Index]Task end at: " + new Date());
    }


    private StringBuilder queueAppIndexJSONPath(String time, String countryCode, String languageCode, GoogleCollectionEnum collection, GoogleCategoryEnum category) {
        StringBuilder path = new StringBuilder(CommonUtils.folderBy(googleRootPath, DataServiceEnum.top_app_daily.name(), DataTypeEnum.queue.name(), time).getAbsolutePath());
        path.append("/");
        path.append(countryCode).append(REGEX_3_UNDER_LINE);
        path.append(languageCode).append(REGEX_3_UNDER_LINE);
        path.append(category.name().toLowerCase()).append(REGEX_3_UNDER_LINE);
        path.append(collection.name().toLowerCase()).append(REGEX_3_UNDER_LINE);
        path.append(time).append(REGEX_3_UNDER_LINE);
        path.append(ZERO_NUMBER).append(JSON_FILE_EXTENSION);
        return path;
    }
}
