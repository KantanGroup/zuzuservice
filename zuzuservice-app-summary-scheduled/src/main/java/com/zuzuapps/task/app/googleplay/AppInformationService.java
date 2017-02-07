package com.zuzuapps.task.app.googleplay;

import com.zuzuapps.task.app.common.CommonUtils;
import com.zuzuapps.task.app.common.DataServiceEnum;
import com.zuzuapps.task.app.common.DataTypeEnum;
import com.zuzuapps.task.app.elasticsearch.models.AppInformationElasticSearch;
import com.zuzuapps.task.app.elasticsearch.models.AppScreenshotElasticSearch;
import com.zuzuapps.task.app.exceptions.ExceptionCodes;
import com.zuzuapps.task.app.exceptions.GooglePlayRuntimeException;
import com.zuzuapps.task.app.googleplay.models.ApplicationPlay;
import com.zuzuapps.task.app.googleplay.models.ScreenshotPlay;
import com.zuzuapps.task.app.googleplay.models.ScreenshotPlays;
import com.zuzuapps.task.app.master.models.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.exception.GenericJDBCException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * @author tuanta17
 */
@Service
public class AppInformationService extends AppCommonService {
    final Log logger = LogFactory.getLog("AppInformationService");

    /**
     * Daily app information update
     */
    public void dailyAppInformationUpdate() {
        while (true) {
            // something that should execute on weekdays only
            String dirPath = CommonUtils.folderBy(rootPath, DataServiceEnum.information.name(), DataTypeEnum.queue.name()).getAbsolutePath();
            File dir = new File(dirPath);
            File[] files = dir.listFiles();
            if (files != null && files.length != 0) {
                processDailyAppInformation(files);
            }
            CommonUtils.delay(timeWaitRuntimeLocal);
        }
    }

    private void processDailyAppInformation(File[] files) {
        logger.debug("[Application Information Store]Task start at: " + new Date());
        String time = CommonUtils.getDailyByTime();
        for (File json : files) {
            logger.debug("[Application Information Store]File " + json.getAbsolutePath());
            String filename = json.getName();
            String[] data = filename.split(REGEX_3_UNDER_LINE);
            if (data.length >= 3) {
                String countryCode = data[0];
                String languageCode = data[1];
                String appId = data[2].replaceAll(JSON_FILE_EXTENSION, "");
                logger.debug("[Application Information Store]Get app " + appId + " by language " + languageCode + " in elastic search");
                AppInformationElasticSearch app = appInformationElasticSearchRepository.findOne(appId + "_" + languageCode);
                if (app == null || isTimeToUpdate(app.getCreateAt())) {
                    try {
                        logger.debug("[Application Information Store]Get app " + appId + " by language " + languageCode);
                        ApplicationPlay applicationPlay = getAppInformationByLanguage(languageCode, appId);
                        if (app == null) {
                            logger.debug("[Application Information Store]App " + appId + " by language " + languageCode + " not found");
                        } else {
                            logger.debug("[Application Information Store]Time to update app " + appId + " by language " + languageCode);
                        }
                        // Get app information
                        app = createAppInformation(applicationPlay, languageCode);
                        // Index to elastic search
                        logger.debug("[Application Information Store]Get app " + appId + " by language " + languageCode);
                        appInformationElasticSearchRepository.save(app);
                        moveFile(json.getAbsolutePath(), CommonUtils.folderBy(rootPath, DataServiceEnum.information.name(), DataTypeEnum.log.name(), time, countryCode).getAbsolutePath());
                    } catch (GooglePlayRuntimeException ex) {
                        if (ex.getCode() == ExceptionCodes.APP_NOT_FOUND) {
                            logger.error("[Application Information Store][" + appId + "][" + languageCode + "]Error " + ex.getMessage());
                        } else if (ex.getCode() == ExceptionCodes.UNKNOWN_EXCEPTION) {
                            logger.error("[Application Information Store][" + appId + "][" + languageCode + "]Error " + ex.getMessage(), ex);
                        } else {
                            logger.warn("[Application Information Store][" + appId + "][" + languageCode + "]Error " + ex.getMessage());
                        }
                        moveFile(json.getAbsolutePath(), CommonUtils.folderBy(rootPath, DataServiceEnum.information.name(), DataTypeEnum.error.name(), time).getAbsolutePath());
                    } catch (Exception ex) {
                        logger.error("[Application Information Store][" + appId + "][" + languageCode + "]Error " + ex.getMessage(), ex);
                        moveFile(json.getAbsolutePath(), CommonUtils.folderBy(rootPath, DataServiceEnum.information.name(), DataTypeEnum.error.name(), time).getAbsolutePath());
                    }
                }
                CommonUtils.delay(timeGetAppInformation);
            } else {
                moveFile(json.getAbsolutePath(), CommonUtils.folderBy(rootPath, DataServiceEnum.information.name(), DataTypeEnum.error.name(), time).getAbsolutePath());
            }
        }
        logger.debug("[Application Information Store]Task end at: " + new Date());
    }

    private AppInformationElasticSearch createAppInformation(ApplicationPlay applicationPlay, String languageCode) {
        AppInformationElasticSearch app = new AppInformationElasticSearch();
        app.setId(applicationPlay.getAppId() + "_" + languageCode);
        app.setAppId(applicationPlay.getAppId());
        app.setUrl(applicationPlay.getUrl());
        app.setTitle(applicationPlay.getTitle());
        app.setSummary(applicationPlay.getSummary());
        app.setDeveloper(applicationPlay.getDeveloper());
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
        app.setGenreId(applicationPlay.getGenreId());
        app.setDescription(applicationPlay.getDescription());
        app.setDescriptionHTML(applicationPlay.getDescriptionHTML());
        app.setFamilyGenre(applicationPlay.getFamilyGenre());
        app.setFamilyGenreId(applicationPlay.getFamilyGenreId());
        app.setOffersIAP(applicationPlay.isOffersIAP());
        app.setAdSupported(applicationPlay.isAdSupported());
        app.setAndroidVersion(applicationPlay.getAndroidVersion());
        app.setAndroidVersionText(applicationPlay.getAndroidVersionText());
        app.setContentRating(applicationPlay.getContentRating());
        app.setScreenshots(applicationPlay.getScreenshots());
        app.setPreregister(applicationPlay.isPreregister());
        app.setVideo(applicationPlay.getVideo());
        app.setPlaystoreUrl(applicationPlay.getPlaystoreUrl());
        app.setPermissions(applicationPlay.getPermissions());
        app.setSimilar(applicationPlay.getSimilar());
        app.setReviews(applicationPlay.getReviews());
        // Update current data
        app.setCreateAt(new Date());
        return app;
    }

    /**
     * Time to update
     *
     * @param appTime App time
     */
    private boolean isTimeToUpdate(Date appTime) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -timeUpdateAppInformation);
        String appTime_ = CommonUtils.getTimeBy(appTime, "yyyyMMdd");
        String currentTime_ = CommonUtils.getTimeBy(cal.getTime(), "yyyyMMdd");
        return appTime_.equalsIgnoreCase(currentTime_);
    }

    /**
     * Get app information by language
     *
     * @param languageCode Language code
     * @param appId        App id
     */
    private ApplicationPlay getAppInformationByLanguage(String languageCode, String appId) throws GooglePlayRuntimeException, IOException {
        ApplicationPlay applicationPlay =
                informationApplicationPlayService.getInformationApplications(appId, languageCode);
        StringBuilder path = createAppInformationJSONPath(appId, languageCode);
        Files.write(Paths.get(path.toString()), mapper.writeValueAsBytes(applicationPlay));
        return applicationPlay;
    }

    private StringBuilder createAppInformationJSONPath(String appId, String languageCode) {
        StringBuilder path = new StringBuilder(CommonUtils.folderBy(rootPath, DataServiceEnum.app.name(), DataTypeEnum.queue.name(), languageCode).getAbsolutePath());
        path.append("/");
        path.append(languageCode).append(REGEX_3_UNDER_LINE);
        path.append(appId.toLowerCase()).append(JSON_FILE_EXTENSION);
        return path;
    }

    /**
     * Get distinct language
     */
    private Set<String> findDistinctByLanguageCode() {
        List<CountryMaster> countries = countryRepository.findAllByTypeGreaterThanOrderByTypeDesc(0);
        Set<String> languages = new HashSet<String>();
        for (CountryMaster country : countries) {
            languages.add(country.getLanguageCode());
        }
        return languages;
    }

    /**
     * Daily app update
     */
    public void dailyAppUpdate() {
        while (true) {
            // something that should execute on weekdays only
            Set<String> languages = findDistinctByLanguageCode();
            for (String languageCode : languages) {
                String dirPath = CommonUtils.folderBy(rootPath, DataServiceEnum.app.name(), DataTypeEnum.queue.name(), languageCode).getAbsolutePath();
                File dir = new File(dirPath);
                File[] files = dir.listFiles();
                if (files != null && files.length != 0) {
                    processDailyApp(files);
                }
            }
            CommonUtils.delay(timeWaitRuntimeLocal);
        }
    }

    private void processDailyApp(File[] files) {
        logger.debug("[Application Store]Cronjob start at: " + new Date());
        for (File json : files) {
            String filename = json.getName();
            String[] data = filename.split(REGEX_3_UNDER_LINE);
            if (data.length >= 2) {
                String appId = data[1];
                String languageCode = data[0];
                try {
                    // 1. Get data
                    ApplicationPlay app = mapper.readValue(json, ApplicationPlay.class);
                    // 2. Write data to database
                    AppMaster appMaster = createAppMaster(app);
                    AppLanguageMaster appLanguageMaster = createAppLanguageMaster(app, languageCode);
                    logger.debug("[Application Store]App " + appId + " info store to database");
                    appLanguageMasterRepository.save(appLanguageMaster);
                    appMasterRepository.save(appMaster);
                    // 3. Index data

                    // 4. Create icon
                    ScreenshotPlay screenshotPlay = screenshotApplicationPlayService.extractOriginalIcon(app.getAppId(), app.getIcon());
                    AppScreenshotMaster appScreenshotMaster = createAppScreenshotMaster(screenshotPlay);
                    logger.debug("[Application Store]Icon store to database");
                    appScreenshotMasterRepository.save(appScreenshotMaster);
                    CommonUtils.delay(timeGetAppScreenshot);
                    // 5. Create screenshot
                    queueAppScreenshot(app);
                    moveFile(json.getAbsolutePath(), CommonUtils.folderBy(rootPath, DataServiceEnum.app.name(), DataTypeEnum.log.name(), CommonUtils.getDailyByTime()).getAbsolutePath());
                } catch (GenericJDBCException ex) {
                    logger.error("[Application Store][" + appId + "][" + languageCode + "]Error " + ex.getMessage());
                    moveFile(json.getAbsolutePath(), CommonUtils.folderBy(rootPath, DataServiceEnum.app.name(), DataTypeEnum.error.name(), CommonUtils.getDailyByTime()).getAbsolutePath());
                } catch (GooglePlayRuntimeException ex) {
                    if (ex.getCode() == ExceptionCodes.UNKNOWN_EXCEPTION) {
                        logger.error("[Application Store][" + appId + "][" + languageCode + "]Error " + ex.getMessage(), ex);
                    } else {
                        logger.warn("[Application Store][" + appId + "][" + languageCode + "]Error " + ex.getMessage());
                    }
                    moveFile(json.getAbsolutePath(), CommonUtils.folderBy(rootPath, DataServiceEnum.app.name(), DataTypeEnum.error.name(), CommonUtils.getDailyByTime()).getAbsolutePath());
                } catch (Exception ex) {
                    logger.error("[Application Store][" + appId + "][" + languageCode + "]Error " + ex.getMessage(), ex);
                    moveFile(json.getAbsolutePath(), CommonUtils.folderBy(rootPath, DataServiceEnum.app.name(), DataTypeEnum.error.name(), CommonUtils.getDailyByTime()).getAbsolutePath());
                }
                CommonUtils.delay(timeGetAppInformation);
            } else {
                moveFile(json.getAbsolutePath(), CommonUtils.folderBy(rootPath, DataServiceEnum.app.name(), DataTypeEnum.error.name(), CommonUtils.getDailyByTime()).getAbsolutePath());
            }
        }
        logger.debug("[Application Store]Cronjob end at: " + new Date());
    }

    private void queueAppScreenshot(ApplicationPlay app) {
        try {
            ScreenshotPlays screenshotPlays = new ScreenshotPlays();
            screenshotPlays.setAppId(app.getAppId());
            screenshotPlays.setScreenshots(app.getScreenshots());
            StringBuilder path = new StringBuilder(CommonUtils.folderBy(rootPath, DataServiceEnum.screenshoot.name(), DataTypeEnum.queue.name()).getAbsolutePath());
            path.append("/").append(app.getAppId()).append(REGEX_3_UNDER_LINE);
            path.append("screenshots").append(JSON_FILE_EXTENSION);
            logger.debug("[Image Store]Write screenshot of app " + app.getAppId().toLowerCase() + " to queue folder " + path.toString());
            Files.write(Paths.get(path.toString()), mapper.writeValueAsBytes(screenshotPlays));
        } catch (Exception ex) {
            logger.error("[Image Store]Write screenshot of app error " + ex.getMessage(), ex);
        }
    }

    /**
     * Split app summary to apps
     */
    public void dailyAppScreenshotUpdate() {
        while (true) {
            // something that should execute on weekdays only
            String dirPath = CommonUtils.folderBy(rootPath, DataServiceEnum.screenshoot.name(), DataTypeEnum.queue.name()).getAbsolutePath();
            File dir = new File(dirPath);
            File[] files = dir.listFiles();
            if (files != null && files.length != 0) {
                processDailyAppScreenshots(files);
            }
            CommonUtils.delay(timeWaitRuntimeLocal);
        }
    }

    private void processDailyAppScreenshots(File[] files) {
        logger.debug("[Screenshot Store]Task start at: " + new Date());
        for (File json : files) {
            String filename = json.getName();
            String[] data = filename.split(REGEX_3_UNDER_LINE);
            if (data.length >= 2) {
                String appId = data[0];
                try {
                    ScreenshotPlays screenshotPlays = mapper.readValue(json, ScreenshotPlays.class);
                    List<AppScreenshotMaster> appScreenshotMasters = new ArrayList<AppScreenshotMaster>();
                    AppScreenshotElasticSearch appScreenshotElasticSearch = new AppScreenshotElasticSearch();
                    appScreenshotElasticSearch.setId(appId);
                    for (String screenshot : screenshotPlays.getScreenshots()) {
                        // 5. Create screenshot
                        ScreenshotPlay screenshotPlay = screenshotApplicationPlayService.extractOriginalScreenshot(appId, screenshot);
                        AppScreenshotMaster appScreenshotMaster = createAppScreenshotMaster(screenshotPlay);
                        appScreenshotMasters.add(appScreenshotMaster);
                        appScreenshotElasticSearch.getScreenshotPlays().add(screenshotPlay);
                        CommonUtils.delay(timeGetAppScreenshot);
                    }
                    appScreenshotMasterRepository.save(appScreenshotMasters);
                    appScreenshotElasticSearchRepository.save(appScreenshotElasticSearch);
                    moveFile(json.getAbsolutePath(), CommonUtils.folderBy(rootPath, DataServiceEnum.screenshoot.name(), DataTypeEnum.log.name(), CommonUtils.getDailyByTime()).getAbsolutePath());
                } catch (GooglePlayRuntimeException ex) {
                    if (ex.getCode() == ExceptionCodes.UNKNOWN_EXCEPTION) {
                        logger.error("[Screenshot Store][" + appId + "]Error " + ex.getMessage(), ex);
                    } else {
                        logger.warn("[Screenshot Store][" + appId + "]Error " + ex.getMessage());
                    }
                    moveFile(json.getAbsolutePath(), CommonUtils.folderBy(rootPath, DataServiceEnum.screenshoot.name(), DataTypeEnum.error.name(), CommonUtils.getDailyByTime()).getAbsolutePath());
                } catch (Exception ex) {
                    logger.error("[Screenshot Store][" + appId + "]Error " + ex.getMessage(), ex);
                    moveFile(json.getAbsolutePath(), CommonUtils.folderBy(rootPath, DataServiceEnum.screenshoot.name(), DataTypeEnum.error.name(), CommonUtils.getDailyByTime()).getAbsolutePath());
                }
                CommonUtils.delay(timeGetAppInformation);
            } else {
                moveFile(json.getAbsolutePath(), CommonUtils.folderBy(rootPath, DataServiceEnum.screenshoot.name(), DataTypeEnum.error.name(), CommonUtils.getDailyByTime()).getAbsolutePath());
            }
        }
        logger.debug("[Screenshot Store]Task end at: " + new Date());
    }

    private AppScreenshotMaster createAppScreenshotMaster(ScreenshotPlay screenshotPlay) {
        AppScreenshotMaster app = new AppScreenshotMaster();
        app.setAppId(screenshotPlay.getAppId());
        app.setSource(screenshotPlay.getSource());
        app.setType((short) screenshotPlay.getType());
        app.setOriginal(screenshotPlay.getOriginal());
        return app;
    }

    private AppLanguageMaster createAppLanguageMaster(ApplicationPlay applicationPlay, String languageCode) {
        AppLanguageMaster app = new AppLanguageMaster();
        AppLanguageId id = new AppLanguageId(applicationPlay.getAppId(), languageCode);
        app.setId(id);
        app.setTitle(applicationPlay.getTitle());
        app.setSummary(applicationPlay.getSummary());
        app.setDescription(applicationPlay.getDescription());
        app.setDescriptionHTML(applicationPlay.getDescriptionHTML());
        return app;
    }

    private AppMaster createAppMaster(ApplicationPlay applicationPlay) {
        AppMaster app = new AppMaster();
        app.setAppId(applicationPlay.getAppId());
        app.setUrl(applicationPlay.getUrl());
        app.setIcon(applicationPlay.getIcon());
        app.setScore(applicationPlay.getScore());
        app.setPrice(applicationPlay.getPrice());
        app.setFree(applicationPlay.isFree());
        app.setDeveloperId(applicationPlay.getDeveloper().getDevId());
        app.setDeveloperUrl(applicationPlay.getDeveloper().getUrl());
        app.setDeveloperEmail(applicationPlay.getDeveloperEmail());
        app.setDeveloperWebsite(applicationPlay.getDeveloperWebsite());
        app.setUpdated(applicationPlay.getUpdated());
        app.setAppVersion(applicationPlay.getVersion());
        app.setMinInstalls(applicationPlay.getMinInstalls());
        app.setMaxInstalls(applicationPlay.getMaxInstalls());
        app.setGenre(applicationPlay.getGenre());
        app.setGenreId(applicationPlay.getGenreId());
        app.setFamilyGenre(applicationPlay.getFamilyGenre());
        app.setFamilyGenreId(applicationPlay.getFamilyGenreId());
        app.setOffersIAP(applicationPlay.isOffersIAP());
        app.setAdSupported(applicationPlay.isAdSupported());
        app.setAndroidVersion(applicationPlay.getAndroidVersion());
        app.setAndroidVersionText(applicationPlay.getAndroidVersionText());
        app.setContentRating(applicationPlay.getContentRating());
        app.setPreregister(applicationPlay.isPreregister());
        app.setVideo(applicationPlay.getVideo());
        app.setPlaystoreUrl(applicationPlay.getPlaystoreUrl());
        app.setPermissions(applicationPlay.getPermissions());
        app.setSimilar(applicationPlay.getSimilar());
        app.setReviews(applicationPlay.getReviews());
        return app;
    }
}
