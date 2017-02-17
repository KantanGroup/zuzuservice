package com.zuzuapps.task.app.googleplay;

import com.zuzuapps.task.app.common.CommonUtils;
import com.zuzuapps.task.app.common.DataServiceEnum;
import com.zuzuapps.task.app.common.DataTypeEnum;
import com.zuzuapps.task.app.exceptions.ExceptionCodes;
import com.zuzuapps.task.app.exceptions.GooglePlayRuntimeException;
import com.zuzuapps.task.app.googleplay.models.ApplicationPlay;
import com.zuzuapps.task.app.googleplay.models.ScreenshotPlays;
import com.zuzuapps.task.app.master.models.AppLanguageId;
import com.zuzuapps.task.app.master.models.AppLanguageMaster;
import com.zuzuapps.task.app.master.models.AppMaster;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.exception.GenericJDBCException;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

/**
 * @author tuanta17
 */
@Service
public class AppService extends AppCommonService {
    final Log logger = LogFactory.getLog("AppService");

    /**
     * Daily app update
     */
    public void dailyAppUpdate() {
        while (true) {
            // something that should execute on weekdays only
            String dirPath = CommonUtils.folderBy(rootPath, DataServiceEnum.app_daily.name(), DataTypeEnum.queue.name()).getAbsolutePath();
            File dir = new File(dirPath);
            File[] files = dir.listFiles();
            if (files != null && files.length != 0) {
                try {
                    CommonUtils.sortFilesOrderByTime(files);
                    processDailyApp(files);
                } catch (Exception ex) {
                    logger.error("[ProcessError]Error " + ex.getMessage(), ex);
                }
            }
            CommonUtils.delay(timeWaitRuntimeLocal);
        }
    }

    /**
     * Summary app update
     */
    public void summaryAppUpdate() {
        while (true) {
            // something that should execute on weekdays only
            String dirPath = CommonUtils.folderBy(rootPath, DataServiceEnum.app_summary.name(), DataTypeEnum.queue.name()).getAbsolutePath();
            File dir = new File(dirPath);
            File[] files = dir.listFiles();
            if (files != null && files.length != 0) {
                try {
                    CommonUtils.sortFilesOrderByTime(files);
                    processDailyApp(files);
                } catch (Exception ex) {
                    logger.error("[ProcessError]Error " + ex.getMessage(), ex);
                }
            }
            CommonUtils.delay(timeWaitRuntimeLocal);
        }
    }

    private void processDailyApp(File[] files) throws Exception {
        logger.debug("[Application Store]Cronjob start at: " + new Date());
        for (File json : files) {
            logger.info("[Application Store]File " + json.getAbsolutePath());
            String filename = json.getName();
            String[] data = filename.split(REGEX_3_UNDER_LINE);
            if (data.length >= 2) {
                String appId = data[1].replaceAll(JSON_FILE_EXTENSION, "");
                String languageCode = data[0];
                long startTime = System.currentTimeMillis();
                try {
                    // 1. Get data
                    ApplicationPlay app = mapper.readValue(json, ApplicationPlay.class);
                    // 2. Write data to database
                    AppMaster appMaster = createAppMaster(app);
                    AppLanguageMaster appLanguageMaster = createAppLanguageMaster(app, languageCode, filename);
                    logger.debug("[Application Store]App " + appId + " info store to database");
                    appLanguageMasterRepository.save(appLanguageMaster);
                    appMasterRepository.save(appMaster);
                    // 3. Index data

                    // 4. Create icon
                    screenshotApplicationPlayService.extractOriginalIcon(app.getAppId(), app.getIcon());
                    // 5. Create screenshot
                    queueAppScreenshot(app);
                    moveFile(json.getAbsolutePath(), CommonUtils.folderBy(rootPath, DataServiceEnum.app.name(), DataTypeEnum.log.name(), CommonUtils.getDailyByTime()).getAbsolutePath());
                } catch (GenericJDBCException ex) {
                    logger.error("[Application Store][" + appId + "][" + languageCode + "]Error " + ex.getMessage());
                    moveFile(json.getAbsolutePath(), CommonUtils.folderBy(rootPath, DataServiceEnum.app.name(), DataTypeEnum.error.name(), CommonUtils.getDailyByTime()).getAbsolutePath());
                } catch (GooglePlayRuntimeException ex) {
                    if (ex.getCode() == ExceptionCodes.NETWORK_LIMITED_EXCEPTION) {
                        logger.info("[Application Store][" + appId + "][" + languageCode + "]Error " + ex.getMessage());
                    } else {
                        moveFile(json.getAbsolutePath(), CommonUtils.folderBy(rootPath, DataServiceEnum.app.name(), DataTypeEnum.error.name(), CommonUtils.getDailyByTime()).getAbsolutePath());
                        if (ex.getCode() == ExceptionCodes.UNKNOWN_EXCEPTION) {
                            logger.error("[Application Store][" + appId + "][" + languageCode + "]Error " + ex.getMessage(), ex);
                        } else {
                            logger.info("[Application Store][" + appId + "][" + languageCode + "]Error " + ex.getMessage());
                        }
                    }
                } catch (Exception ex) {
                    logger.error("[Application Store][" + appId + "][" + languageCode + "]Error " + ex.getMessage(), ex);
                    moveFile(json.getAbsolutePath(), CommonUtils.folderBy(rootPath, DataServiceEnum.app.name(), DataTypeEnum.error.name(), CommonUtils.getDailyByTime()).getAbsolutePath());
                }
                long delayTime = System.currentTimeMillis() - startTime;
                CommonUtils.delay(timeGetAppInformation - delayTime);
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

    private AppLanguageMaster createAppLanguageMaster(ApplicationPlay applicationPlay, String languageCode, String filename) {
        StringBuilder path = new StringBuilder(DataServiceEnum.app.name());
        path.append("/").append(DataTypeEnum.log.name());
        path.append("/").append(CommonUtils.getDailyByTime());
        path.append("/").append(filename);
        path.append(".gz");
        AppLanguageMaster app = new AppLanguageMaster();
        AppLanguageId id = new AppLanguageId(applicationPlay.getAppId(), languageCode);
        app.setId(id);
        app.setPath(path.toString());
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
        app.setDeveloperId(new String(Base64.encode(applicationPlay.getDeveloper().getDevId().getBytes())));
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
