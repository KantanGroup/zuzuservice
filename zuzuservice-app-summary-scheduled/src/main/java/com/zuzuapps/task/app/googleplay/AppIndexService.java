package com.zuzuapps.task.app.googleplay;

import com.zuzuapps.task.app.common.*;
import com.zuzuapps.task.app.elasticsearch.models.AppIndexElasticSearch;
import com.zuzuapps.task.app.googleplay.models.SummaryApplicationPlay;
import com.zuzuapps.task.app.googleplay.models.SummaryApplicationPlays;
import com.zuzuapps.task.app.master.models.AppIndexMaster;
import com.zuzuapps.task.app.master.models.CountryMaster;
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

    /**
     * Write app index of category in to json
     */
    public void appIndexStoreData() {
        logger.info("[Application Index Store]Cronjob start at: " + new Date());
        // something that should execute on weekdays only
        String time = CommonUtils.getDailyByTime();
        List<CountryMaster> countries = countryRepository.findAllByTypeGreaterThanOrderByTypeDesc(0);
        for (CountryMaster countryMaster : countries) {
            for (CollectionEnum collection : CollectionEnum.values()) {
                for (CategoryEnum category : CategoryEnum.values()) {
                    try {
                        SummaryApplicationPlays summaryApplicationPlays
                                = summaryApplicationPlayService.getSummaryApplications(category, collection, countryMaster.getLanguageCode(), countryMaster.getCountryCode(), 0);
                        StringBuilder path = queueAppIndexJSONPath(time, countryMaster, collection, category);
                        logger.debug("[Application Index Store]Write app summary to json " + path.toString());
                        Files.write(Paths.get(path.toString()), mapper.writeValueAsBytes(summaryApplicationPlays));
                    } catch (Exception ex) {
                        logger.error("[Application Index Store][" + countryMaster.getCountryCode() + "][" + category.name() + "][" + collection.name() + "]Error " + ex.getMessage(), ex);
                    }
                    CommonUtils.delay(timeGetAppSummary);
                }
            }
        }
        logger.info("[Application Index Store]Cronjob end at: " + new Date());
    }

    private StringBuilder queueAppIndexJSONPath(String time, CountryMaster countryMaster, CollectionEnum collection, CategoryEnum category) {
        StringBuilder path = new StringBuilder(CommonUtils.folderBy(rootPath, DataServiceEnum.top.name(), DataTypeEnum.queue.name(), time).getAbsolutePath());
        path.append("/");
        path.append(countryMaster.getCountryCode()).append(REGEX_3_UNDER_LINE);
        path.append(countryMaster.getLanguageCode()).append(REGEX_3_UNDER_LINE);
        path.append(category.name().toLowerCase()).append(REGEX_3_UNDER_LINE);
        path.append(collection.name().toLowerCase()).append(REGEX_3_UNDER_LINE);
        path.append(time).append(REGEX_3_UNDER_LINE);
        path.append(ZERO_NUMBER).append(JSON_FILE_EXTENSTION);
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
                processIndexUpdate(files);
            }
            CommonUtils.delay(timeWaitRuntimeLocal);
        }
    }

    /**
     * App index update
     *
     * @param files File data
     */
    public void processIndexUpdate(File[] files) {
        logger.info("[Application Summary --> Index]Cronjob start at: " + new Date());
        // something that should execute on weekdays only
        String time = CommonUtils.getDailyByTime();
        for (File json : files) {
            logger.debug("[Application Summary --> Index]File " + json.getAbsolutePath());
            List<AppIndexMaster> appIndexMasters = new ArrayList<AppIndexMaster>();
            List<AppIndexElasticSearch> appIndexElasticSearches = new ArrayList<AppIndexElasticSearch>();
            String filename = json.getName();
            String[] data = filename.split(REGEX_3_UNDER_LINE);
            if (data.length >= 4) {
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
                        createAppIndexElasticSearch(appIndexElasticSearches, countryCode, category, collection, fileDateTime, index, app);
                        index++;
                    }
                    // Create app info json
                    queueAppInformation(apps.getResults(), countryCode, languageCode);
                    // Add data to mysql
                    logger.debug("[Application Summary --> Index]Store to database");
                    appIndexMasterRepository.save(appIndexMasters);
                    // Add data to ElasticSearch
                    logger.debug("[Application Summary --> Index]Index to elastichsearch");
                    appIndexElasticSearchRepository.save(appIndexElasticSearches);
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
        logger.info("[Application Summary --> Index]Cronjob end at: " + new Date());
    }

    private void createAppIndexMaster(List<AppIndexMaster> appIndexMasters, String country, CategoryEnum category, CollectionEnum collection, Date fileDateTime, short index, SummaryApplicationPlay app) {
        AppIndexMaster appIndexMaster = new AppIndexMaster();
        appIndexMaster.setAppId(app.getAppId());
        appIndexMaster.setCategory(category);
        appIndexMaster.setCollection(collection);
        appIndexMaster.setCountryCode(country);
        appIndexMaster.setIndex(index);
        appIndexMaster.setIcon(app.getIcon());
        appIndexMaster.setCreateAt(fileDateTime);
        appIndexMasters.add(appIndexMaster);
    }

    private void createAppIndexElasticSearch(List<AppIndexElasticSearch> appIndexElasticSearches, String country, CategoryEnum category, CollectionEnum collection, Date fileDateTime, int index, SummaryApplicationPlay app) {
        AppIndexElasticSearch appIndexElasticSearch = new AppIndexElasticSearch();
        appIndexElasticSearch.setId(country + "_" + category.name().toLowerCase() + "_" + collection.name() + "_" + index);
        appIndexElasticSearch.setIndex(index);
        appIndexElasticSearch.setTitle(app.getTitle());
        appIndexElasticSearch.setAppId(app.getAppId());
        appIndexElasticSearch.setCategory(category.name().toLowerCase());
        appIndexElasticSearch.setCollection(collection.name());
        appIndexElasticSearch.setCountryCode(country);
        appIndexElasticSearch.setIndex(index);
        appIndexElasticSearch.setIcon(app.getIcon());
        appIndexElasticSearch.setVisible(true);
        appIndexElasticSearch.setCreateAt(fileDateTime);
        appIndexElasticSearch.setUpdateAt(fileDateTime);
        appIndexElasticSearches.add(appIndexElasticSearch);
    }
}
