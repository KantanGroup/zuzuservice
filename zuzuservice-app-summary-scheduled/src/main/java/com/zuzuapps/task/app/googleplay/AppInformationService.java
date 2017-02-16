package com.zuzuapps.task.app.googleplay;

import com.zuzuapps.task.app.common.CommonUtils;
import com.zuzuapps.task.app.common.DataServiceEnum;
import com.zuzuapps.task.app.common.DataTypeEnum;
import com.zuzuapps.task.app.exceptions.ExceptionCodes;
import com.zuzuapps.task.app.exceptions.GooglePlayRuntimeException;
import com.zuzuapps.task.app.googleplay.models.ApplicationPlay;
import com.zuzuapps.task.app.solr.models.AppInformationSolr;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

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
                try {
                    CommonUtils.sortFilesOrderByTime(files);
                    processDailyAppInformation(files);
                } catch (Exception ex) {
                    logger.error("[ProcessError]Error " + ex.getMessage(), ex);
                }
            }
            CommonUtils.delay(timeWaitRuntimeLocal);
        }
    }

    private void processDailyAppInformation(File[] files) throws Exception {
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
                    AppInformationSolr app = appInformationService.findOne(appId + "_" + languageCode);
                    if (app == null || isTimeToUpdate(app.getCreateAt())) {
                        logger.debug("[Information Store]Get app " + appId + " by language " + languageCode);
                        ApplicationPlay applicationPlay = getAppInformationByLanguage(languageCode, appId);
                        if (app == null) {
                            logger.debug("[Information Store]App " + appId + " by language " + languageCode + " not found");
                        } else {
                            logger.debug("[Information Store]Time to update app " + appId + " by language " + languageCode);
                        }
                        // Get app information
                        app = createAppInformation(applicationPlay, languageCode);
                        // Index to elastic search
                        logger.debug("[Information Store]Save app " + appId + " by language " + languageCode);
                        appInformationService.save(app);
                        moveFile(json.getAbsolutePath(), CommonUtils.folderBy(rootPath, DataServiceEnum.information.name(), DataTypeEnum.log.name(), time, countryCode).getAbsolutePath());
                    }
                } catch (GooglePlayRuntimeException ex) {
                    if (ex.getCode() == ExceptionCodes.NETWORK_LIMITED_EXCEPTION) {
                        logger.info("[Information Store][" + appId + "][" + languageCode + "]Error " + ex.getMessage());
                    } else {
                        moveFile(json.getAbsolutePath(), CommonUtils.folderBy(rootPath, DataServiceEnum.information.name(), DataTypeEnum.error.name(), time).getAbsolutePath());
                        if (ex.getCode() == ExceptionCodes.UNKNOWN_EXCEPTION) {
                            logger.error("[Information Store][" + appId + "][" + languageCode + "]Error " + ex.getMessage(), ex);
                        } else {
                            logger.info("[Information Store][" + appId + "][" + languageCode + "]Error " + ex.getMessage());
                        }
                    }
                } catch (Exception ex) {
                    logger.error("[Information Store][" + appId + "][" + languageCode + "]Error " + ex.getMessage(), ex);
                    moveFile(json.getAbsolutePath(), CommonUtils.folderBy(rootPath, DataServiceEnum.information.name(), DataTypeEnum.error.name(), time).getAbsolutePath());
                }
                long delayTime = System.currentTimeMillis() - startTime;
                CommonUtils.delay(timeGetAppInformation - delayTime);
            } else {
                moveFile(json.getAbsolutePath(), CommonUtils.folderBy(rootPath, DataServiceEnum.information.name(), DataTypeEnum.error.name(), time).getAbsolutePath());
            }
        }
        logger.debug("[Information Store]Task end at: " + new Date());
    }

    private AppInformationSolr createAppInformation(ApplicationPlay applicationPlay, String languageCode) {
        AppInformationSolr app = new AppInformationSolr();
        app.setId(applicationPlay.getAppId() + "_" + languageCode);
        app.setAppId(applicationPlay.getAppId());
        app.setUrl(applicationPlay.getUrl());
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
     * Get app information by language
     *
     * @param languageCode Language code
     * @param appId        App id
     */
    private ApplicationPlay getAppInformationByLanguage(String languageCode, String appId) throws Exception {
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
}
