package com.zuzuapps.task.app.googlestore;

import com.zuzuapps.task.app.appstore.models.AppIndexMaster;
import com.zuzuapps.task.app.common.*;
import com.zuzuapps.task.app.googlestore.common.GoogleCollectionEnum;
import com.zuzuapps.task.app.googlestore.common.GoogleCategoryEnum;
import com.zuzuapps.task.app.googlestore.models.SummaryApplicationGooglePlay;
import com.zuzuapps.task.app.googlestore.models.SummaryApplicationGooglePlays;
import com.zuzuapps.task.app.solr.googlestore.models.GoogleAppIndexSolr;
import com.zuzuapps.task.app.solr.googlestore.models.GoogleAppTrendSolr;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.common.util.Base64;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author tuanta17
 */
@Service
public class GoogleAppIndexStoreService extends GoogleAppCommonService {
    final Log logger = LogFactory.getLog("GoogleAppIndexStoreService");

    /**
     * Daily app index update
     */
    public void dailyAppIndexUpdate() {
        while (true) {
            // something that should execute on weekdays only
            String time = CommonUtils.getDailyByTime();
            String dirPath = CommonUtils.folderBy(googleRootPath, DataServiceEnum.top_app_daily.name(), DataTypeEnum.queue.name(), time).getAbsolutePath();
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
        logger.debug("[Application Index Store]Cronjob start at: " + new Date());
        // something that should execute on weekdays only
        String time = CommonUtils.getDailyByTime();
        for (File json : files) {
            logger.info("[Application Index Store]File " + json.getAbsolutePath());
            String filename = json.getName();
            String[] data = filename.split(REGEX_3_UNDER_LINE);
            if (data.length >= 4) {
                List<AppIndexMaster> appIndexDatabase = new ArrayList<AppIndexMaster>();
                List<GoogleAppIndexSolr> appIndexSolr = new ArrayList<GoogleAppIndexSolr>();
                List<GoogleAppTrendSolr> appTrendSolr = new ArrayList<GoogleAppTrendSolr>();
                String countryCode = data[0];
                String languageCode = data[1];
                GoogleCategoryEnum category = GoogleCategoryEnum.valueOf(data[2].toUpperCase());
                GoogleCollectionEnum collection = GoogleCollectionEnum.valueOf(data[3]);
                String toDate = data[4];
                try {
                    logger.debug("[Application Index Store]Convert json data to object");
                    SummaryApplicationGooglePlays apps = mapper.readValue(json, SummaryApplicationGooglePlays.class);
                    short index = 1;
                    for (SummaryApplicationGooglePlay app : apps.getResults()) {
                        createAppIndexMaster(appIndexDatabase, countryCode, category, collection, index, app);
                        createAppIndexInSearchEngine(appIndexSolr, countryCode, category, collection, index, app);
                        createAppTrendInSearchEngine(appTrendSolr, countryCode, category, collection, index, app, toDate);
                        index++;
                    }
                    // Add data to mysql
                    //logger.debug("[Application Index Store]Store to database");
                    //appIndexDatabaseService.save(appIndexDatabase);
                    // Add data to Apache Solr
                    logger.debug("[Application Index Store]Index to search engine");
                    appIndexService.save(appIndexSolr);
                    logger.debug("[Application Index Store]Trend to search engine");
                    appTrendService.save(appTrendSolr);
                    // Create app info json
                    queueAppInformation(apps.getResults(), countryCode, languageCode, DataServiceEnum.information_daily);
                    // Remove data
                    FileUtils.deleteQuietly(json);
                } catch (Exception ex) {
                    logger.error("[Application Index Store][" + countryCode + "][" + category.name() + "][" + collection.name() + "]Error " + ex.getMessage(), ex);
                    moveFile(json.getAbsolutePath(), CommonUtils.folderBy(googleRootPath, DataServiceEnum.top_app_daily.name(), DataTypeEnum.error.name(), time).getAbsolutePath());
                }
            } else {
                FileUtils.deleteQuietly(json);
            }
            CommonUtils.delay(5);
        }
        logger.debug("[Application Index Store]Cronjob end at: " + new Date());
    }

    private void createAppIndexMaster(List<AppIndexMaster> appIndexMasters, String countryCode, GoogleCategoryEnum category, GoogleCollectionEnum collection, short index, SummaryApplicationGooglePlay app) {
        AppIndexMaster appIndexMaster = new AppIndexMaster();
        appIndexMaster.setId(countryCode + "_" + category.name().toLowerCase() + "_" + collection.name() + "_" + index);
        appIndexMaster.setAppId(app.getAppId());
        appIndexMaster.setCategory(category);
        appIndexMaster.setCollection(collection);
        appIndexMaster.setCountryCode(countryCode);
        appIndexMaster.setIndex(index);
        appIndexMaster.setIcon(app.getIcon());
        appIndexMaster.setDeveloperId(Base64.byteArrayToBase64(app.getDeveloper().getDevId().getBytes()));
        appIndexMaster.setFree(app.isFree());
        appIndexMaster.setPrice(app.getPrice());
        appIndexMaster.setScore(app.getScore());
        appIndexMasters.add(appIndexMaster);
    }

    private void createAppIndexInSearchEngine(List<GoogleAppIndexSolr> appIndexs, String countryCode, GoogleCategoryEnum category, GoogleCollectionEnum collection, int index, SummaryApplicationGooglePlay app) {
        GoogleAppIndexSolr appIndex = new GoogleAppIndexSolr();
        appIndex.setId(countryCode + "_" + category.name().toLowerCase() + "_" + collection.name() + "_" + index);
        appIndex.setIndex(index);
        appIndex.setTitle(app.getTitle());
        appIndex.setAppId(app.getAppId());
        appIndex.setCategory(category.name().toLowerCase());
        appIndex.setCollection(collection.name());
        appIndex.setCountryCode(countryCode);
        appIndex.setIndex(index);
        appIndex.setIcon(app.getIcon());
        appIndex.setDeveloperId(app.getDeveloper().getDevId());
        appIndex.setFree(app.isFree());
        appIndex.setPrice(app.getPrice());
        appIndex.setScore(app.getScore());
        appIndexs.add(appIndex);
    }

    private void createAppTrendInSearchEngine(List<GoogleAppTrendSolr> appTrends, String countryCode, GoogleCategoryEnum category, GoogleCollectionEnum collection, int index, SummaryApplicationGooglePlay app, String toDate) {
        GoogleAppTrendSolr appTrend = new GoogleAppTrendSolr();
        appTrend.setId(countryCode + "_" + category.name().toLowerCase() + "_" + collection.name() + "_" + app.getAppId() + "_" + toDate);
        appTrend.setIndex(index);
        appTrend.setAppId(app.getAppId());
        appTrend.setCategory(category.name().toLowerCase());
        appTrend.setCollection(collection.name());
        appTrend.setCountryCode(countryCode);
        appTrend.setIndex(index);
        appTrend.setFree(app.isFree());
        appTrend.setPrice(app.getPrice());
        appTrend.setScore(app.getScore());
        appTrend.setCreateAt(CommonUtils.toDate(toDate));
        appTrends.add(appTrend);
    }
}
