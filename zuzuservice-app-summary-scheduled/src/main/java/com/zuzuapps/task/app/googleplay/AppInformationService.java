package com.zuzuapps.task.app.googleplay;

import com.zuzuapps.task.app.common.CommonUtils;
import com.zuzuapps.task.app.common.DataServiceEnum;
import com.zuzuapps.task.app.common.DataTypeEnum;
import com.zuzuapps.task.app.elasticsearch.models.AppInformationElasticSearch;
import com.zuzuapps.task.app.exceptions.ExceptionCodes;
import com.zuzuapps.task.app.exceptions.GooglePlayRuntimeException;
import com.zuzuapps.task.app.googleplay.models.ApplicationPlay;
import com.zuzuapps.task.app.master.models.CountryMaster;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
     * Split app summary to apps
     */
    public void dailyAppInformationUpdate() {
        while (true) {
            // something that should execute on weekdays only
            String dirPath = CommonUtils.folderBy(rootPath, DataServiceEnum.information.name(), DataTypeEnum.queue.name()).getAbsolutePath();
            File dir = new File(dirPath);
            File[] files = dir.listFiles();
            if (files != null && files.length != 0) {
                queueAppInformation(files);
            }
            CommonUtils.delay(timeWaitRuntimeLocal);
        }
    }

    private void queueAppInformation(File[] files) {
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
                    } catch (GooglePlayRuntimeException ex) {
                        if (ex.getCode() == ExceptionCodes.UNKNOWN_EXCEPTION) {
                            logger.error("[Application Information Store][" + appId + "][" + languageCode + "]Error " + ex.getMessage(), ex);
                        } else {
                            logger.warn("[Application Information Store][" + appId + "][" + languageCode + "]Error " + ex.getMessage());
                        }
                    } catch (Exception ex) {
                        logger.error("[Application Information Store][" + appId + "][" + languageCode + "]Error " + ex.getMessage(), ex);
                    }
                }
                // Move log folder
                logger.debug("[Application Information Store]Move " + appId + " to log folder");
                moveFile(json.getAbsolutePath(), CommonUtils.folderBy(rootPath, DataServiceEnum.information.name(), DataTypeEnum.log.name(), time, countryCode).getAbsolutePath());
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
     * Split app summary to apps
     */
    public void dailyAppLanguageUpdate() {
        while (true) {
            // something that should execute on weekdays only
            Set<String> languages = findDistinctByLanguageCode();
            for (String languageCode : languages) {
                String dirPath = CommonUtils.folderBy(rootPath, DataServiceEnum.app.name(), DataTypeEnum.queue.name(), languageCode).getAbsolutePath();
                File dir = new File(dirPath);
                File[] files = dir.listFiles();
                if (files != null && files.length != 0) {
                    queueAppLanguage(files);
                }
            }
            CommonUtils.delay(timeWaitRuntimeLocal);
        }
    }

    private void queueAppLanguage(File[] files) {
        logger.debug("[Application Language Store]Cronjob start at: " + new Date());
        for (File json : files) {
            // 1. Get data

            // 2. Write data to database

            // 3. Index data

            // 4. Create icon

            // 5. Create screen shoot
        }
        logger.debug("[Application Language Store]Cronjob end at: " + new Date());
    }
}
