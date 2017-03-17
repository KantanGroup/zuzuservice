package com.zuzuapps.task.app.appstore;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zuzuapps.task.app.AppCommonService;
import com.zuzuapps.task.app.appstore.models.ApplicationAppStore;
import com.zuzuapps.task.app.appstore.models.SummaryApplicationAppStore;
import com.zuzuapps.task.app.appstore.services.InformationApplicationAppStoreService;
import com.zuzuapps.task.app.appstore.services.SummaryApplicationAppStoreService;
import com.zuzuapps.task.app.common.CommonUtils;
import com.zuzuapps.task.app.common.DataServiceEnum;
import com.zuzuapps.task.app.common.DataTypeEnum;
import com.zuzuapps.task.app.exceptions.AppStoreRuntimeException;
import com.zuzuapps.task.app.exceptions.ExceptionCodes;
import com.zuzuapps.task.app.solr.appstore.models.AppleAppInformationSolr;
import com.zuzuapps.task.app.solr.appstore.repositories.AppleAppIndexSolrRepository;
import com.zuzuapps.task.app.solr.appstore.repositories.AppleAppInformationSolrRepository;
import com.zuzuapps.task.app.solr.appstore.repositories.AppleAppTrendSolrRepository;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
public class AppleAppCommonService extends AppCommonService {
    protected final Log logger = LogFactory.getLog("AppleAppCommonService");

    @Value("${apple.data.root.path:/tmp/appstore}")
    protected String appleRootPath;

    @Value("${apple.data.image.path:/tmp/appstore/imagestore}")
    protected String appleImageStore;

    @Autowired
    protected AppleAppIndexSolrRepository appleAppIndexSolrService;
    @Autowired
    protected AppleAppTrendSolrRepository appleAppTrendSolrService;
    @Autowired
    protected AppleAppInformationSolrRepository appleAppInformationSolrService;
    @Autowired
    protected SummaryApplicationAppStoreService summaryApplicationAppStoreService;
    @Autowired
    protected InformationApplicationAppStoreService informationApplicationAppStoreService;

    protected void queueAppInformation(List<SummaryApplicationAppStore> summaryApplicationAppStores, String countryCode, String languageCode, DataServiceEnum information) throws JsonProcessingException {
        for (SummaryApplicationAppStore summaryApplication : summaryApplicationAppStores) {
            try {
                if (checkAppInformationSolr(summaryApplication.getId() + "_" + languageCode)) {
                    StringBuilder path = new StringBuilder(CommonUtils.folderBy(appleRootPath, information.name(), DataTypeEnum.queue.name()).getAbsolutePath());
                    path.append("/").append(countryCode).append(REGEX_3_UNDER_LINE);
                    path.append(languageCode).append(REGEX_3_UNDER_LINE);
                    path.append(summaryApplication.getId()).append(JSON_FILE_EXTENSION);
                    Files.write(Paths.get(path.toString()), mapper.writeValueAsBytes(summaryApplication));
                }
            } catch (Exception ex) {
                logger.error("Write summary of app error " + ex.getMessage(), ex);
            }
            CommonUtils.delay(10);
        }
    }

    /**
     * Check app information avaiable or not
     */
    private boolean checkAppInformationSolr(String id) {
        AppleAppInformationSolr app = appleAppInformationSolrService.findOne(id);
        if (app == null || isTimeToUpdate(app.getCreateAt())) {
            return true;
        } else {
            CommonUtils.delay(10);
            return false;
        }
    }

    protected void processAppInformation(File[] files, boolean isDaily) throws Exception {
        logger.debug("[AppleAppInformationDailyService][Information Store]Task start at: " + new Date());
        String time = CommonUtils.getDailyByTime();
        for (File json : files) {
            logger.info("[AppleAppInformationDailyService][Information Store]File " + json.getAbsolutePath());
            String filename = json.getName();
            String[] data = filename.split(REGEX_3_UNDER_LINE);
            if (data.length >= 2) {
                String countryCode = data[0];
                String aid = data[1].replaceAll(JSON_FILE_EXTENSION, "");
                logger.debug("[AppleAppInformationDailyService][Information Store]Get app " + aid + " by country " + countryCode + " in elastic search");
                long startTime = System.currentTimeMillis();
                try {
                    if (checkAppInformationSolr(aid + "_" + countryCode)) {
                        extractAppInformation(countryCode, aid, isDaily);
                        long delayTime = System.currentTimeMillis() - startTime;
                        CommonUtils.delay(timeGetAppInformation - delayTime);
                    }
                    FileUtils.deleteQuietly(json);
                } catch (AppStoreRuntimeException ex) {
                    if (ex.getCode() == ExceptionCodes.NETWORK_LIMITED_EXCEPTION) {
                        logger.info("[AppleAppInformationDailyService][Information Store][" + aid + "][" + countryCode + "]Error " + ex.getMessage());
                    } else if (ex.getCode() == ExceptionCodes.APP_NOT_FOUND) {
                        extractEmptyAppInformation(aid, countryCode);
                        moveFile(json.getAbsolutePath(), CommonUtils.folderBy(appleRootPath, DataServiceEnum.information.name(), DataTypeEnum.not_found.name()).getAbsolutePath());
                        logger.info("[AppleAppInformationDailyService][Information Store][" + aid + "][" + countryCode + "]Error " + ex.getMessage());
                    } else {
                        moveFile(json.getAbsolutePath(), CommonUtils.folderBy(appleRootPath, DataServiceEnum.information.name(), DataTypeEnum.error.name()).getAbsolutePath());
                        if (ex.getCode() == ExceptionCodes.UNKNOWN_EXCEPTION) {
                            logger.error("[AppleAppInformationDailyService][Information Store][" + aid + "][" + countryCode + "]Error " + ex.getMessage(), ex);
                        } else {
                            logger.info("[AppleAppInformationDailyService][Information Store][" + aid + "][" + countryCode + "]Error " + ex.getMessage());
                        }
                    }
                } catch (Exception ex) {
                    logger.error("[AppleAppInformationDailyService][Information Store][" + aid + "][" + countryCode + "]Error " + ex.getMessage(), ex);
                    moveFile(json.getAbsolutePath(), CommonUtils.folderBy(appleRootPath, DataServiceEnum.information.name(), DataTypeEnum.error.name()).getAbsolutePath());
                }
            } else {
                FileUtils.deleteQuietly(json);
            }
        }
        logger.debug("[AppleAppInformationDailyService][Information Store]Task end at: " + new Date());
    }

    protected void extractAppInformation(String countryCode, String aid, boolean isDaily) throws Exception {
        ApplicationAppStore application = getAppInformationByCountry(countryCode, aid, isDaily);
        // Get app information
        AppleAppInformationSolr app = createAppInformation(application, countryCode);
        // Index to Solr
        appleAppInformationSolrService.save(app);
        // 4. Create icon
        screenshotApplicationPlayService.extractOriginalIcon(appleImageStore, app.getId(), app.getIcon());
    }

    protected void extractEmptyAppInformation(String aid, String countryCode) throws Exception {
        // Get app information
        AppleAppInformationSolr app = new AppleAppInformationSolr();
        app.setId(aid + "_" + countryCode);
        app.setAppId(aid);
        app.setDescription("App not found");
        // Update current data
        app.setCreateAt(new Date());
        // Index to Solr
        appleAppInformationSolrService.save(app);
    }

    /**
     * Get app information by country
     *
     * @param countryCode Country code
     * @param aid       App id
     */
    protected ApplicationAppStore getAppInformationByCountry(String countryCode, String aid, boolean isDaily) throws Exception {
        ApplicationAppStore application =
                informationApplicationAppStoreService.getInformationApplications(aid, countryCode);
        StringBuilder path = createAppInformationJSONPath(aid, countryCode, isDaily);
        System.out.println(path);
        System.out.println(mapper.writeValueAsString(application));
        // Files.write(Paths.get(path.toString()), mapper.writeValueAsBytes(application));
        FileUtils.writeByteArrayToFile(new File(path.toString()), mapper.writeValueAsBytes(application));
        return application;
    }

    protected StringBuilder createAppInformationJSONPath(String aid, String countryCode, boolean isDaily) {
        StringBuilder path;
        if (isDaily) {
            path = new StringBuilder(CommonUtils.folderBy(appleRootPath, DataServiceEnum.app_daily.name(), DataTypeEnum.queue.name()).getAbsolutePath());
        } else {
            path = new StringBuilder(CommonUtils.folderBy(appleRootPath, DataServiceEnum.app_summary.name(), DataTypeEnum.queue.name()).getAbsolutePath());
        }
        path.append("/");
        path.append(countryCode).append(REGEX_3_UNDER_LINE);
        path.append(aid).append(JSON_FILE_EXTENSION);
        return path;
    }


    protected AppleAppInformationSolr createAppInformation(ApplicationAppStore application, String countryCode) {
        AppleAppInformationSolr app = new AppleAppInformationSolr();
        app.setId(application.getId() + "_" + countryCode);
        app.setAid(application.getId());
        app.setAppId(application.getAppId());
        app.setTitle(application.getTitle());
        app.setGenres(application.getGenres());
        app.setGenreIds(application.getGenreIds());
        app.setPrimaryGenre(application.getPrimaryGenre());
        app.setPrimaryGenreId(application.getPrimaryGenreId());
        app.setDescription(application.getDescription());
        app.setDeveloperId(application.getDeveloper());
        app.setDeveloper(application.getDeveloper());
        app.setDeveloperUrl(application.getDeveloperUrl());
        app.setDeveloperWebsite(application.getDeveloperWebsite());
        app.setIcon(application.getIcon());
        app.setScore(application.getScore());
        app.setPrice(application.getPrice());
        app.setCurrency(application.getCurrency());
        app.setFree(application.isFree());
        app.setContentRating(application.getContentRating());
        app.setLanguages(application.getLanguages());
        app.setSize(application.getSize());
        app.setRequiredOsVersion(application.getRequiredOsVersion());
        app.setReleased(application.getReleased());
        app.setUpdated(application.getUpdated());
        app.setVersion(application.getVersion());
        app.setCurrentVersionScore(application.getCurrentVersionScore());
        app.setCurrentVersionReviews(application.getCurrentVersionReviews());
        app.setScreenshots(application.getScreenshots());
        app.setIpadScreenshots(application.getIpadScreenshots());
        app.setAppletvScreenshots(application.getAppletvScreenshots());
        app.setSupportedDevices(application.getSupportedDevices());
        // Update current data
        app.setCreateAt(new Date());
        return app;
    }
}
