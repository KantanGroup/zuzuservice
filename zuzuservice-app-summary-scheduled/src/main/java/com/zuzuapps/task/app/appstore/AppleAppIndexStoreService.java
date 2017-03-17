package com.zuzuapps.task.app.appstore;

import com.zuzuapps.task.app.appstore.common.AppleCategoryEnum;
import com.zuzuapps.task.app.appstore.common.AppleCollectionEnum;
import com.zuzuapps.task.app.appstore.models.SummaryApplicationAppStore;
import com.zuzuapps.task.app.appstore.models.SummaryApplicationAppStores;
import com.zuzuapps.task.app.common.CommonUtils;
import com.zuzuapps.task.app.common.DataServiceEnum;
import com.zuzuapps.task.app.common.DataTypeEnum;
import com.zuzuapps.task.app.solr.appstore.models.AppleAppIndexSolr;
import com.zuzuapps.task.app.solr.appstore.models.AppleAppTrendSolr;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author tuanta17
 */
public class AppleAppIndexStoreService extends AppleAppCommonService {
    final Log logger = LogFactory.getLog("AppleAppIndexStoreService");

    /**
     * Daily app index update
     */
    public void dailyAppIndexUpdate() {
        while (true) {
            // something that should execute on weekdays only
            String time = CommonUtils.getDailyByTime();
            String dirPath = CommonUtils.folderBy(appleRootPath, DataServiceEnum.top_app_daily.name(), DataTypeEnum.queue.name(), time).getAbsolutePath();
            File dir = new File(dirPath);
            File[] files = dir.listFiles();
            if (files != null && files.length != 0) {
                try {
                    CommonUtils.sortFilesOrderByTime(files);
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
        logger.debug("[AppleAppIndexStoreService][Index Store]Cronjob start at: " + new Date());
        // something that should execute on weekdays only
        String time = CommonUtils.getDailyByTime();
        for (File json : files) {
            logger.info("[AppleAppIndexStoreService][Index Store]File " + json.getAbsolutePath());
            String filename = json.getName();
            String[] data = filename.split(REGEX_3_UNDER_LINE);
            if (data.length >= 4) {
                // List<AppIndexMaster> appIndexDatabase = new ArrayList<AppIndexMaster>();
                List<AppleAppIndexSolr> appIndexSolr = new ArrayList<AppleAppIndexSolr>();
                List<AppleAppTrendSolr> appTrendSolr = new ArrayList<AppleAppTrendSolr>();
                String countryCode = data[0];
                String languageCode = data[1];
                AppleCategoryEnum category = AppleCategoryEnum.valueOf(data[2].toUpperCase());
                AppleCollectionEnum collection = AppleCollectionEnum.valueOf(data[3]);
                String toDate = data[4];
                try {
                    logger.debug("[AppleAppIndexStoreService][Index Store]Convert json data to object");
                    SummaryApplicationAppStores apps = mapper.readValue(json, SummaryApplicationAppStores.class);
                    short index = 1;
                    for (SummaryApplicationAppStore app : apps.getResults()) {
                        // createAppIndexMaster(appIndexDatabase, countryCode, category, collection, index, app);
                        createAppIndexInSearchEngine(appIndexSolr, countryCode, category, collection, index, app);
                        createAppTrendInSearchEngine(appTrendSolr, countryCode, category, collection, index, app, toDate);
                        index++;
                    }
                    // Add data to mysql
                    //logger.debug("[AppleAppIndexStoreService][Index Store]Store to database");
                    //appIndexDatabaseService.save(appIndexDatabase);
                    // Add data to Apache Solr
                    logger.debug("[AppleAppIndexStoreService][Index Store]Index to search engine");
                    appleAppIndexSolrService.save(appIndexSolr);
                    logger.debug("[AppleAppIndexStoreService][Index Store]Trend to search engine");
                    appleAppTrendSolrService.save(appTrendSolr);
                    // Create app info json
                    queueAppInformation(apps.getResults(), countryCode, languageCode, DataServiceEnum.information_daily);
                    // Remove data
                    FileUtils.deleteQuietly(json);
                } catch (Exception ex) {
                    logger.error("[AppleAppIndexStoreService][Index Store][" + countryCode + "][" + category.name() + "][" + collection.name() + "]Error " + ex.getMessage(), ex);
                    moveFile(json.getAbsolutePath(), CommonUtils.folderBy(appleRootPath, DataServiceEnum.top_app_daily.name(), DataTypeEnum.error.name(), time).getAbsolutePath());
                }
            } else {
                FileUtils.deleteQuietly(json);
            }
            CommonUtils.delay(5);
        }
        logger.debug("[AppleAppIndexStoreService][Index Store]Cronjob end at: " + new Date());
    }

    private void createAppIndexInSearchEngine(List<AppleAppIndexSolr> appIndexs, String countryCode, AppleCategoryEnum category, AppleCollectionEnum collection, int index, SummaryApplicationAppStore app) {
        AppleAppIndexSolr appIndex = new AppleAppIndexSolr();
        appIndex.setId(countryCode + "_" + category.name().toLowerCase() + "_" + collection.name() + "_" + index);
        appIndex.setAid(app.getId());
        appIndex.setAppId(app.getAppId());
        appIndex.setTitle(app.getTitle());
        appIndex.setCategory(category.name().toLowerCase());
        appIndex.setCollection(collection.name());
        appIndex.setCountryCode(countryCode);
        appIndex.setIndex(index);
        appIndex.setIcon(app.getIcon());
        appIndex.setDeveloper(app.getDeveloper());
        appIndex.setFree(app.isFree());
        appIndex.setPrice(app.getPrice());
        appIndexs.add(appIndex);
    }

    private void createAppTrendInSearchEngine(List<AppleAppTrendSolr> appTrends, String countryCode, AppleCategoryEnum category, AppleCollectionEnum collection, int index, SummaryApplicationAppStore app, String toDate) {
        AppleAppTrendSolr appTrend = new AppleAppTrendSolr();
        appTrend.setId(countryCode + "_" + category.name().toLowerCase() + "_" + collection.name() + "_" + app.getId() + "_" + toDate);
        appTrend.setAid(app.getId());
        appTrend.setAppId(app.getAppId());
        appTrend.setCategory(category.name().toLowerCase());
        appTrend.setCollection(collection.name());
        appTrend.setCountryCode(countryCode);
        appTrend.setIndex(index);
        appTrend.setCreateAt(CommonUtils.toDate(toDate));
        appTrends.add(appTrend);
    }
}
