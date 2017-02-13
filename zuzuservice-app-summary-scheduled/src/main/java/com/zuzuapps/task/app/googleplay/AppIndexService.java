package com.zuzuapps.task.app.googleplay;

import com.zuzuapps.task.app.common.*;
import com.zuzuapps.task.app.exceptions.ExceptionCodes;
import com.zuzuapps.task.app.exceptions.GooglePlayRuntimeException;
import com.zuzuapps.task.app.googleplay.models.SummaryApplicationPlay;
import com.zuzuapps.task.app.googleplay.models.SummaryApplicationPlays;
import com.zuzuapps.task.app.master.models.AppIndexMaster;
import com.zuzuapps.task.app.master.models.CountryMaster;
import com.zuzuapps.task.app.solr.models.AppIndexSolr;
import com.zuzuapps.task.app.solr.models.AppTrendSolr;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
public class AppIndexService extends AppCommonService {
    final Log logger = LogFactory.getLog("AppIndexService");

    public void generateAppIndexStore() {
        logger.info("[Application Index Generation]Task start at: " + new Date());
        String time = CommonUtils.getDailyByTime();
        String dirPath = CommonUtils.folderBy(rootPath, DataServiceEnum.top.name(), DataTypeEnum.generate.name(), time).getAbsolutePath();
        File dir = new File(dirPath);
        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            List<CountryMaster> countries = countryRepository.findAllByTypeGreaterThanOrderByTypeDesc(0);
            for (CountryMaster countryMaster : countries) {
                for (CollectionEnum collection : CollectionEnum.values()) {
                    CommonUtils.createFile(Paths.get(dirPath, countryMaster.getCountryCode() + REGEX_3_UNDER_LINE + countryMaster.getLanguageCode() + REGEX_3_UNDER_LINE + collection.name() + REGEX_3_UNDER_LINE + CategoryEnum.ALL.name().toLowerCase() + REGEX_3_UNDER_LINE + time));
                    for (CategoryEnum category : CategoryEnum.values()) {
                        CommonUtils.createFile(Paths.get(dirPath, countryMaster.getCountryCode() + REGEX_3_UNDER_LINE + countryMaster.getLanguageCode() + REGEX_3_UNDER_LINE + collection.name() + REGEX_3_UNDER_LINE + category.name().toLowerCase() + REGEX_3_UNDER_LINE + time));
                    }
                }
            }
        }
        logger.info("[Application Index Generation]Task end at: " + new Date());
    }

    /**
     * Write app index of category in to json
     */
    public void processAppIndexStoreData() {
        while (true) {
            // something that should execute on weekdays only
            String time = CommonUtils.getDailyByTime();
            String dirPath = CommonUtils.folderBy(rootPath, DataServiceEnum.top.name(), DataTypeEnum.generate.name(), time).getAbsolutePath();
            File dir = new File(dirPath);
            File[] files = dir.listFiles();
            if (files != null && files.length != 0) {
                try {
                    processAppIndexStoreData(files);
                } catch (Exception ex) {
                    logger.error("[ProcessError]Error " + ex.getMessage(), ex);
                }
            }
            CommonUtils.delay(timeWaitRuntimeLocal);
        }
    }

    private void processAppIndexStoreData(File[] files) throws Exception {
        logger.info("[Application Index Store]Task start at: " + new Date());
        // something that should execute on weekdays only
        String time = CommonUtils.getDailyByTime();
        for (File json : files) {
            logger.info("[Application Information Store]File " + json.getAbsolutePath());
            String filename = json.getName();
            String[] data = filename.split(REGEX_3_UNDER_LINE);
            if (data.length >= 4) {
                String countryCode = data[0];
                String languageCode = data[1];
                CollectionEnum collection = CollectionEnum.valueOf(data[2]);
                CategoryEnum category = CategoryEnum.valueOf(data[3].toUpperCase());
                try {
                    SummaryApplicationPlays summaryApplicationPlays
                            = summaryApplicationPlayService.getSummaryApplications(category, collection, languageCode, countryCode, 0);
                    StringBuilder path = queueAppIndexJSONPath(time, countryCode, languageCode, collection, category);
                    logger.debug("[Application Index Store]Write app summary to json " + path.toString());
                    Files.write(Paths.get(path.toString()), mapper.writeValueAsBytes(summaryApplicationPlays));
                    logger.debug("[Application Index Store]Delete file " + json.getAbsolutePath());
                } catch (GooglePlayRuntimeException ex) {
                    if (ex.getCode() == ExceptionCodes.UNKNOWN_EXCEPTION) {
                        logger.error("[Application Index Store][" + category.name() + "][" + collection.name() + "]Error " + ex.getMessage());
                    } else {
                        logger.info("[Application Index Store][" + category.name() + "][" + collection.name() + "]Error " + ex.getMessage());
                    }
                } catch (Exception ex) {
                    logger.error("[Application Index Store][" + countryCode + "][" + category.name() + "][" + collection.name() + "]Error " + ex.getMessage(), ex);
                }
                CommonUtils.delay(timeGetAppSummary);
            }
            FileUtils.deleteQuietly(json);
            CommonUtils.delay(5);
        }
        logger.info("[Application Index Store]Task end at: " + new Date());
    }


    private StringBuilder queueAppIndexJSONPath(String time, String countryCode, String languageCode, CollectionEnum collection, CategoryEnum category) {
        StringBuilder path = new StringBuilder(CommonUtils.folderBy(rootPath, DataServiceEnum.top.name(), DataTypeEnum.queue.name(), time).getAbsolutePath());
        path.append("/");
        path.append(countryCode).append(REGEX_3_UNDER_LINE);
        path.append(languageCode).append(REGEX_3_UNDER_LINE);
        path.append(category.name().toLowerCase()).append(REGEX_3_UNDER_LINE);
        path.append(collection.name().toLowerCase()).append(REGEX_3_UNDER_LINE);
        path.append(time).append(REGEX_3_UNDER_LINE);
        path.append(ZERO_NUMBER).append(JSON_FILE_EXTENSION);
        return path;
    }

    /**
     * Daily app index update
     */
    public void dailyAppIndexUpdate() {
        while (true) {
            // something that should execute on weekdays only
            String time = CommonUtils.getDailyByTime();
            String dirPath = CommonUtils.folderBy(rootPath, DataServiceEnum.top.name(), DataTypeEnum.queue.name(), time).getAbsolutePath();
            File dir = new File(dirPath);
            File[] files = dir.listFiles();
            if (files != null && files.length != 0) {
                try {
                    processIndexUpdate(files);
                } catch (Exception ex) {
                    logger.error("[ProcessError]Error " + ex.getMessage(), ex);
                }
            }
            CommonUtils.delay(timeWaitRuntimeLocal);
        }
    }

    /**
     * App index update
     *
     * @param files File data
     */
    public void processIndexUpdate(File[] files) throws Exception {
        logger.debug("[Application Summary --> Index]Cronjob start at: " + new Date());
        // something that should execute on weekdays only
        String time = CommonUtils.getDailyByTime();
        for (File json : files) {
            logger.info("[Application Summary --> Index]File " + json.getAbsolutePath());
            String filename = json.getName();
            String[] data = filename.split(REGEX_3_UNDER_LINE);
            if (data.length >= 4) {
                List<AppIndexMaster> appIndexMasters = new ArrayList<AppIndexMaster>();
                List<AppIndexSolr> appIndexs = new ArrayList<AppIndexSolr>();
                List<AppTrendSolr> appTrends = new ArrayList<AppTrendSolr>();
                String countryCode = data[0];
                String languageCode = data[1];
                CategoryEnum category = CategoryEnum.valueOf(data[2].toUpperCase());
                CollectionEnum collection = CollectionEnum.valueOf(data[3]);
                String fileTime = data[4];
                Date fileDateTime = CommonUtils.toDate(fileTime);
                try {
                    logger.debug("[Application Summary --> Index]Convert json data to object");
                    SummaryApplicationPlays apps = mapper.readValue(json, SummaryApplicationPlays.class);
                    short index = 1;
                    for (SummaryApplicationPlay app : apps.getResults()) {
                        createAppIndexMaster(appIndexMasters, countryCode, category, collection, fileDateTime, index, app);
                        createAppIndexElasticSearch(appIndexs, countryCode, category, collection, fileDateTime, index, app);
                        createAppTrendElasticSearch(appTrends, countryCode, category, collection, fileDateTime, index, app);
                        index++;
                    }
                    // Create app info json
                    queueAppInformation(apps.getResults(), countryCode, languageCode);
                    // Add data to mysql
                    logger.debug("[Application Summary --> Index]Store to database");
                    appIndexMasterRepository.save(appIndexMasters);
                    // Add data to ElasticSearch
                    logger.debug("[Application Summary --> Index]Index to elastichsearch");
                    appIndexService.save(appIndexs);
                    logger.debug("[Application Summary --> Index]Trend to elastichsearch");
                    appTrendService.save(appTrends);
                    // Move data to log folder
                    moveFile(json.getAbsolutePath(), CommonUtils.folderBy(rootPath, DataServiceEnum.summary.name(), DataTypeEnum.log.name(), time, countryCode).getAbsolutePath());
                } catch (Exception ex) {
                    logger.error("[Application Summary --> Index][" + countryCode + "][" + category.name() + "][" + collection.name() + "]Error " + ex.getMessage(), ex);
                    moveFile(json.getAbsolutePath(), CommonUtils.folderBy(rootPath, DataServiceEnum.summary.name(), DataTypeEnum.error.name(), time).getAbsolutePath());
                }
            } else {
                moveFile(json.getAbsolutePath(), CommonUtils.folderBy(rootPath, DataServiceEnum.summary.name(), DataTypeEnum.error.name(), time).getAbsolutePath());
            }
            CommonUtils.delay(5);
        }
        logger.debug("[Application Summary --> Index]Cronjob end at: " + new Date());
    }

    private void createAppIndexMaster(List<AppIndexMaster> appIndexMasters, String country, CategoryEnum category, CollectionEnum collection, Date fileDateTime, short index, SummaryApplicationPlay app) {
        AppIndexMaster appIndexMaster = new AppIndexMaster();
        appIndexMaster.setAppId(app.getAppId());
        appIndexMaster.setCategory(category);
        appIndexMaster.setCollection(collection);
        appIndexMaster.setCountryCode(country);
        appIndexMaster.setIndex(index);
        appIndexMaster.setIcon(app.getIcon());
        appIndexMasters.add(appIndexMaster);
    }

    private void createAppIndexElasticSearch(List<AppIndexSolr> appIndexs, String country, CategoryEnum category, CollectionEnum collection, Date fileDateTime, int index, SummaryApplicationPlay app) {
        AppIndexSolr appIndex = new AppIndexSolr();
        appIndex.setId(country + "_" + category.name().toLowerCase() + "_" + collection.name() + "_" + index);
        appIndex.setIndex(index);
        appIndex.setTitle(app.getTitle());
        appIndex.setAppId(app.getAppId());
        appIndex.setCategory(category.name().toLowerCase());
        appIndex.setCollection(collection.name());
        appIndex.setCountryCode(country);
        appIndex.setIndex(index);
        appIndex.setIcon(app.getIcon());
        appIndexs.add(appIndex);
    }

    private void createAppTrendElasticSearch(List<AppTrendSolr> appTrends, String country, CategoryEnum category, CollectionEnum collection, Date fileDateTime, int index, SummaryApplicationPlay app) {
        AppTrendSolr appTrend = new AppTrendSolr();
        appTrend.setId(country + "_" + category.name().toLowerCase() + "_" + collection.name() + "_" + app.getAppId() + "_" + CommonUtils.getTimeBy(fileDateTime, "yyyyMMdd"));
        appTrend.setIndex(index);
        appTrend.setTitle(app.getTitle());
        appTrend.setAppId(app.getAppId());
        appTrend.setCategory(category.name().toLowerCase());
        appTrend.setCollection(collection.name());
        appTrend.setCountryCode(country);
        appTrend.setIndex(index);
        appTrend.setIcon(app.getIcon());
        appTrend.setCreateAt(fileDateTime);
        appTrends.add(appTrend);
    }
}
