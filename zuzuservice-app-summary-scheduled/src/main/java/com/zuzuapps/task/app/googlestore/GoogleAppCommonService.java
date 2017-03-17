package com.zuzuapps.task.app.googlestore;

import com.zuzuapps.task.app.AppCommonService;
import com.zuzuapps.task.app.appstore.models.AppScreenshotMaster;
import com.zuzuapps.task.app.appstore.repositories.AppIndexMasterRepository;
import com.zuzuapps.task.app.appstore.repositories.AppLanguageMasterRepository;
import com.zuzuapps.task.app.appstore.repositories.AppMasterRepository;
import com.zuzuapps.task.app.appstore.repositories.AppScreenshotMasterRepository;
import com.zuzuapps.task.app.common.CommonUtils;
import com.zuzuapps.task.app.common.DataServiceEnum;
import com.zuzuapps.task.app.common.DataTypeEnum;
import com.zuzuapps.task.app.common.ImageTypeEnum;
import com.zuzuapps.task.app.exceptions.ExceptionCodes;
import com.zuzuapps.task.app.exceptions.GooglePlayRuntimeException;
import com.zuzuapps.task.app.googlestore.models.ApplicationGooglePlay;
import com.zuzuapps.task.app.googlestore.models.ScreenshotGooglePlays;
import com.zuzuapps.task.app.googlestore.models.ScreenshotObject;
import com.zuzuapps.task.app.googlestore.models.SummaryApplicationGooglePlay;
import com.zuzuapps.task.app.googlestore.services.InformationApplicationGooglePlayService;
import com.zuzuapps.task.app.googlestore.services.SummaryApplicationGooglePlayService;
import com.zuzuapps.task.app.solr.googlestore.models.GoogleAppInformationSolr;
import com.zuzuapps.task.app.solr.googlestore.models.GoogleAppScreenshotSolr;
import com.zuzuapps.task.app.solr.googlestore.repositories.GoogleAppIndexSolrRepository;
import com.zuzuapps.task.app.solr.googlestore.repositories.GoogleAppInformationSolrRepository;
import com.zuzuapps.task.app.solr.googlestore.repositories.GoogleAppScreenshotSolrRepository;
import com.zuzuapps.task.app.solr.googlestore.repositories.GoogleAppTrendSolrRepository;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
public class GoogleAppCommonService extends AppCommonService {
    final Log logger = LogFactory.getLog("GoogleAppCommonService");

    @Value("${google.data.root.path:/tmp/googlestore}")
    protected String googleRootPath;

    @Value("${google.data.image.path:/tmp/googlestore/imagestore}")
    protected String googleImageStore;

    @Autowired
    protected SummaryApplicationGooglePlayService summaryApplicationPlayService;
    @Autowired
    protected AppIndexMasterRepository appIndexDatabaseService;
    @Autowired
    protected AppMasterRepository appMasterRepository;
    @Autowired
    protected AppLanguageMasterRepository appLanguageMasterRepository;
    @Autowired
    protected AppScreenshotMasterRepository appScreenshotMasterRepository;
    @Autowired
    protected GoogleAppIndexSolrRepository appIndexService;
    @Autowired
    protected GoogleAppTrendSolrRepository appTrendService;
    @Autowired
    protected GoogleAppInformationSolrRepository appInformationService;
    @Autowired
    protected GoogleAppScreenshotSolrRepository appScreenshotSolrService;
    @Autowired
    protected InformationApplicationGooglePlayService informationApplicationPlayService;

    protected void queueAppInformation(List<SummaryApplicationGooglePlay> summaryApplicationPlays, String countryCode, String languageCode, DataServiceEnum information) {
        for (SummaryApplicationGooglePlay summaryApplicationPlay : summaryApplicationPlays) {
            try {
                if (checkAppInformationSolr(summaryApplicationPlay.getAppId() + "_" + languageCode)) {
                    StringBuilder path = new StringBuilder(CommonUtils.folderBy(googleRootPath, information.name(), DataTypeEnum.queue.name()).getAbsolutePath());
                    path.append("/").append(countryCode).append(REGEX_3_UNDER_LINE);
                    path.append(languageCode).append(REGEX_3_UNDER_LINE);
                    path.append(summaryApplicationPlay.getAppId()).append(JSON_FILE_EXTENSION);
                    Files.write(Paths.get(path.toString()), mapper.writeValueAsBytes(summaryApplicationPlay));
                }
            } catch (Exception ex) {
                logger.error("Write summary of app error " + ex.getMessage(), ex);
            }
        }
    }

    /**
     * Check app information avaiable or not
     */
    private boolean checkAppInformationSolr(String id) {
        GoogleAppInformationSolr app = appInformationService.findOne(id);
        if (app == null || isTimeToUpdate(app.getCreateAt())) {
            return true;
        } else {
            CommonUtils.delay(10);
            return false;
        }
    }

    protected GoogleAppInformationSolr createAppInformation(ApplicationGooglePlay applicationPlay, String languageCode) {
        GoogleAppInformationSolr app = new GoogleAppInformationSolr();
        app.setId(applicationPlay.getAppId() + "_" + languageCode);
        app.setAppId(applicationPlay.getAppId());
        app.setTitle(applicationPlay.getTitle());
        app.setSummary(applicationPlay.getSummary());
        app.setDeveloperId(applicationPlay.getDeveloper().getDevId());
        app.setDeveloperUrl(applicationPlay.getDeveloper().getUrl());
        app.setIcon(applicationPlay.getIcon());
        app.setScore(applicationPlay.getScore());
        app.setPrice(applicationPlay.getPrice());
        app.setFree(applicationPlay.isFree());
        app.setDeveloperEmail(applicationPlay.getDeveloperEmail());
        app.setDeveloperWebsite(applicationPlay.getDeveloperWebsite());
        app.setUpdated(applicationPlay.getUpdated());
        app.setVersion(applicationPlay.getVersion());
        app.setMinInstalls(applicationPlay.getMinInstalls());
        app.setMaxInstalls(applicationPlay.getMaxInstalls());
        app.setGenre(applicationPlay.getGenre());
        app.setDescriptionHTML(applicationPlay.getDescriptionHTML());
        app.setFamilyGenre(applicationPlay.getFamilyGenre());
        app.setAndroidVersion(applicationPlay.getAndroidVersion());
        app.setAndroidVersionText(applicationPlay.getAndroidVersionText());
        app.setContentRating(applicationPlay.getContentRating());
        app.setScreenshots(applicationPlay.getScreenshots());
        app.setVideo(applicationPlay.getVideo());
        app.setPlaystoreUrl(applicationPlay.getPlaystoreUrl());
        // Update current data
        app.setCreateAt(new Date());
        return app;
    }

    /**
     * Get app information by language
     *
     * @param languageCode Language code
     * @param appId        App id
     */
    protected ApplicationGooglePlay getAppInformationByLanguage(String languageCode, String appId, boolean isDaily) throws Exception {
        ApplicationGooglePlay applicationPlay =
                informationApplicationPlayService.getInformationApplications(appId, languageCode);
        StringBuilder path = createAppInformationJSONPath(appId, languageCode, isDaily);
        Files.write(Paths.get(path.toString()), mapper.writeValueAsBytes(applicationPlay));
        return applicationPlay;
    }

    protected StringBuilder createAppInformationJSONPath(String appId, String languageCode, boolean isDaily) {
        StringBuilder path;
        if (isDaily) {
            path = new StringBuilder(CommonUtils.folderBy(googleRootPath, DataServiceEnum.app_daily.name(), DataTypeEnum.queue.name()).getAbsolutePath());
        } else {
            path = new StringBuilder(CommonUtils.folderBy(googleRootPath, DataServiceEnum.app_summary.name(), DataTypeEnum.queue.name()).getAbsolutePath());
        }
        path.append("/");
        path.append(languageCode).append(REGEX_3_UNDER_LINE);
        path.append(appId).append(JSON_FILE_EXTENSION);
        return path;
    }

    protected void extractAppInformation(String languageCode, String appId, boolean isDaily) throws Exception {
        ApplicationGooglePlay applicationPlay = getAppInformationByLanguage(languageCode, appId, isDaily);
        // Get app information
        GoogleAppInformationSolr app = createAppInformation(applicationPlay, languageCode);
        // Index to Solr
        appInformationService.save(app);
        // 4. Create icon
        screenshotApplicationPlayService.extractOriginalIcon(googleImageStore, app.getAppId(), app.getIcon());
    }

    protected void extractEmptyAppInformation(String appId, String languageCode) throws Exception {
        // Get app information
        GoogleAppInformationSolr app = new GoogleAppInformationSolr();
        app.setId(appId + "_" + languageCode);
        app.setAppId(appId);
        app.setSummary("App not found");
        // Update current data
        app.setCreateAt(new Date());
        // Index to Solr
        appInformationService.save(app);
    }

    protected void processAppInformation(File[] files, boolean isDaily) throws Exception {
        logger.debug("[Information Store]Task start at: " + new Date());
        String time = CommonUtils.getDailyByTime();
        for (File json : files) {
            logger.info("[Information Store]File " + json.getAbsolutePath());
            String filename = json.getName();
            String[] data = filename.split(REGEX_3_UNDER_LINE);
            if (data.length >= 3) {
                String countryCode = data[0];
                String languageCode = data[1];
                String appId = data[2].replaceAll(JSON_FILE_EXTENSION, "");
                logger.debug("[Information Store]Get app " + appId + " by language " + languageCode + " in elastic search");
                long startTime = System.currentTimeMillis();
                try {
                    if (checkAppInformationSolr(appId + "_" + languageCode)) {
                        extractAppInformation(languageCode, appId, isDaily);
                        long delayTime = System.currentTimeMillis() - startTime;
                        CommonUtils.delay(timeGetAppInformation - delayTime);
                    }
                    FileUtils.deleteQuietly(json);
                } catch (GooglePlayRuntimeException ex) {
                    if (ex.getCode() == ExceptionCodes.NETWORK_LIMITED_EXCEPTION) {
                        logger.info("[Information Store][" + appId + "][" + languageCode + "]Error " + ex.getMessage());
                    } else if (ex.getCode() == ExceptionCodes.APP_NOT_FOUND) {
                        extractEmptyAppInformation(appId, languageCode);
                        moveFile(json.getAbsolutePath(), CommonUtils.folderBy(googleRootPath, DataServiceEnum.information.name(), DataTypeEnum.not_found.name()).getAbsolutePath());
                        logger.info("[Information Store][" + appId + "][" + languageCode + "]Error " + ex.getMessage());
                    } else {
                        moveFile(json.getAbsolutePath(), CommonUtils.folderBy(googleRootPath, DataServiceEnum.information.name(), DataTypeEnum.error.name()).getAbsolutePath());
                        if (ex.getCode() == ExceptionCodes.UNKNOWN_EXCEPTION) {
                            logger.error("[Information Store][" + appId + "][" + languageCode + "]Error " + ex.getMessage(), ex);
                        } else {
                            logger.info("[Information Store][" + appId + "][" + languageCode + "]Error " + ex.getMessage());
                        }
                    }
                } catch (Exception ex) {
                    logger.error("[Information Store][" + appId + "][" + languageCode + "]Error " + ex.getMessage(), ex);
                    moveFile(json.getAbsolutePath(), CommonUtils.folderBy(googleRootPath, DataServiceEnum.information.name(), DataTypeEnum.error.name()).getAbsolutePath());
                }
            } else {
                FileUtils.deleteQuietly(json);
            }
        }
        logger.debug("[Information Store]Task end at: " + new Date());
    }

    protected void processDailyAppScreenshots(File[] files) throws Exception {
        logger.debug("[Screenshot Store]Task start at: " + new Date());
        for (File json : files) {
            logger.info("[Screenshot Store]File " + json.getAbsolutePath());
            String filename = json.getName();
            String[] data = filename.split(REGEX_3_UNDER_LINE);
            if (data.length >= 2) {
                String appId = data[0];
                try {
                    GoogleAppScreenshotSolr screenshotObject = appScreenshotSolrService.findOne(appId);
                    if (screenshotObject == null || isTimeToUpdate(screenshotObject.getCreateAt())) {
                        // Remove all old screenshot
                        CommonUtils.deleteDirectory(CommonUtils.folderBy(googleImageStore, ImageTypeEnum.screenshot.name(), appId));
                        ScreenshotGooglePlays screenshotPlays = mapper.readValue(json, ScreenshotGooglePlays.class);
                        List<ScreenshotObject> screenshotObjects = new ArrayList<ScreenshotObject>();
                        AppScreenshotMaster appScreenshotMaster = new AppScreenshotMaster();
                        appScreenshotMaster.setAppId(appId);
                        GoogleAppScreenshotSolr appScreenshotSolr = new GoogleAppScreenshotSolr();
                        appScreenshotSolr.setId(appId);
                        for (String screenshotLink : screenshotPlays.getScreenshots()) {
                            // 5. Create screenshot
                            ScreenshotObject screenshotPlay = screenshotApplicationPlayService.extractOriginalScreenshot(googleImageStore, appId, screenshotLink);
                            screenshotObjects.add(screenshotPlay);
                            appScreenshotSolr.getScreenshotOrigins().add(screenshotPlay.getOriginal());
                            appScreenshotSolr.getScreenshotSources().add(screenshotPlay.getSource());
                            CommonUtils.delay(timeGetAppScreenshot);
                        }
                        appScreenshotMaster.setData(mapper.writeValueAsString(screenshotObjects));
                        appScreenshotMasterRepository.save(appScreenshotMaster);
                        appScreenshotSolrService.save(appScreenshotSolr);
                        moveFile(json.getAbsolutePath(), CommonUtils.folderBy(googleRootPath, DataServiceEnum.screenshot.name(), DataTypeEnum.log.name()).getAbsolutePath());
                    }
                    FileUtils.deleteQuietly(json);
                } catch (GooglePlayRuntimeException ex) {
                    if (ex.getCode() == ExceptionCodes.NETWORK_LIMITED_EXCEPTION) {
                        logger.info("[Screenshot Store][" + appId + "]Error " + ex.getMessage());
                    } else {
                        moveFile(json.getAbsolutePath(), CommonUtils.folderBy(googleRootPath, DataServiceEnum.screenshot.name(), DataTypeEnum.error.name()).getAbsolutePath());
                        if (ex.getCode() == ExceptionCodes.UNKNOWN_EXCEPTION) {
                            logger.error("[Screenshot Store][" + appId + "]Error " + ex.getMessage(), ex);
                        } else {
                            logger.info("[Screenshot Store][" + appId + "]Error " + ex.getMessage());
                        }
                    }
                } catch (Exception ex) {
                    logger.error("[Screenshot Store][" + appId + "]Error " + ex.getMessage(), ex);
                    moveFile(json.getAbsolutePath(), CommonUtils.folderBy(googleRootPath, DataServiceEnum.screenshot.name(), DataTypeEnum.error.name()).getAbsolutePath());
                }
            } else {
                FileUtils.deleteQuietly(json);
            }
        }
        logger.debug("[Screenshot Store]Task end at: " + new Date());
    }
}
