package com.zuzuapps.task.app.googleplay;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zuzuapps.task.app.appstore.models.AppScreenshotMaster;
import com.zuzuapps.task.app.appstore.models.CountryMaster;
import com.zuzuapps.task.app.appstore.repositories.AppIndexMasterRepository;
import com.zuzuapps.task.app.appstore.repositories.AppLanguageMasterRepository;
import com.zuzuapps.task.app.appstore.repositories.AppMasterRepository;
import com.zuzuapps.task.app.appstore.repositories.AppScreenshotMasterRepository;
import com.zuzuapps.task.app.common.*;
import com.zuzuapps.task.app.exceptions.ExceptionCodes;
import com.zuzuapps.task.app.exceptions.GooglePlayRuntimeException;
import com.zuzuapps.task.app.googleplay.models.ApplicationPlay;
import com.zuzuapps.task.app.googleplay.models.ScreenshotPlay;
import com.zuzuapps.task.app.googleplay.models.ScreenshotPlays;
import com.zuzuapps.task.app.googleplay.models.SummaryApplicationPlay;
import com.zuzuapps.task.app.googleplay.servies.InformationApplicationPlayService;
import com.zuzuapps.task.app.googleplay.servies.ScreenshotApplicationPlayService;
import com.zuzuapps.task.app.googleplay.servies.SummaryApplicationPlayService;
import com.zuzuapps.task.app.solr.models.AppInformationSolr;
import com.zuzuapps.task.app.solr.models.AppScreenshotSolr;
import com.zuzuapps.task.app.solr.repositories.AppIndexSolrRepository;
import com.zuzuapps.task.app.solr.repositories.AppInformationSolrRepository;
import com.zuzuapps.task.app.solr.repositories.AppScreenshotSolrRepository;
import com.zuzuapps.task.app.solr.repositories.AppTrendSolrRepository;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

/**
 * @author tuanta17
 */
@Service
public class AppCommonService {
    protected static final String JSON_FILE_EXTENSION = ".json";
    protected static final String GZ_FILE_EXTENSION = ".gz";
    protected static final String ZERO_NUMBER = "0";
    protected static final String REGEX_3_UNDER_LINE = "___";
    protected static final String COUNTRY_CODE_DEFAULT = "us";
    protected static final String LANGUAGE_CODE_DEFAULT = "en";
    protected final Log logger = LogFactory.getLog("AppCommonService");
    protected final ObjectMapper mapper = new ObjectMapper();

    @Value("${data.root.path:/tmp}")
    protected String rootPath;

    @Value("${data.image.path:/tmp}")
    protected String imageStore;

    @Value("${time.get.app.information:2000}")
    protected long timeGetAppInformation;

    @Value("${time.get.app.summary:4000}")
    protected long timeGetAppSummary;

    @Value("${time.wait.runtime.local:200}")
    protected long timeWaitRuntimeLocal;

    @Value("${time.update.app.information:7}")
    protected int timeUpdateAppInformation;

    @Value("${time.get.app.screenshot:1000}")
    protected int timeGetAppScreenshot;

    @Autowired
    protected SummaryApplicationPlayService summaryApplicationPlayService;
    @Autowired
    protected AppIndexMasterRepository appIndexDatabaseService;
    @Autowired
    protected AppMasterRepository appMasterRepository;
    @Autowired
    protected AppLanguageMasterRepository appLanguageMasterRepository;
    @Autowired
    protected AppScreenshotMasterRepository appScreenshotMasterRepository;
    @Autowired
    protected AppIndexSolrRepository appIndexService;
    @Autowired
    protected AppTrendSolrRepository appTrendService;
    @Autowired
    protected AppInformationSolrRepository appInformationService;
    @Autowired
    protected AppScreenshotSolrRepository appScreenshotSolrService;
    @Autowired
    protected InformationApplicationPlayService informationApplicationPlayService;
    @Autowired
    protected ScreenshotApplicationPlayService screenshotApplicationPlayService;

    protected void queueAppInformation(List<SummaryApplicationPlay> summaryApplicationPlays, String countryCode, String languageCode, DataServiceEnum information) {
        for (SummaryApplicationPlay summaryApplicationPlay : summaryApplicationPlays) {
            try {
                StringBuilder path = new StringBuilder(CommonUtils.folderBy(rootPath, information.name(), DataTypeEnum.queue.name()).getAbsolutePath());
                path.append("/").append(countryCode).append(REGEX_3_UNDER_LINE);
                path.append(languageCode).append(REGEX_3_UNDER_LINE);
                path.append(summaryApplicationPlay.getAppId().toLowerCase()).append(JSON_FILE_EXTENSION);
                //logger.debug("Write summary of app " + summaryApplicationPlay.getAppId().toLowerCase() + " to queue folder " + path.toString());
                Files.write(Paths.get(path.toString()), mapper.writeValueAsBytes(summaryApplicationPlay));
            } catch (Exception ex) {
                logger.error("Write summary of app error " + ex.getMessage(), ex);
            }
        }
    }

    protected void moveFile(String source, String destination) {
        try {
            Path src = Paths.get(source);
            Path des = Paths.get(destination);
            logger.debug("Move json file " + source + " to log folder " + destination);
            Files.move(src, des.resolve(src.getFileName()), StandardCopyOption.REPLACE_EXISTING);
            Path inputFile = Paths.get(des.toFile().getAbsolutePath(), src.getFileName().toString());
            Path zipFile = Paths.get(des.toFile().getAbsolutePath(), src.getFileName() + GZ_FILE_EXTENSION);
            logger.debug("Zip json file " + inputFile + " to file " + zipFile);
            new GZipUtil().gzip(inputFile.toFile().getAbsolutePath(), zipFile.toFile().getAbsolutePath());
            logger.debug("Remove json file " + inputFile);
            Files.delete(inputFile);
        } catch (Exception ex) {
            logger.info("Move json file error " + ex.getMessage());
        }
    }

    /**
     * Time to update
     *
     * @param appTime App time
     */
    protected boolean isTimeToUpdate(Date appTime) {
        if (appTime == null) return true;
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -timeUpdateAppInformation);
        return appTime.before(cal.getTime());
    }

    /**
     * Get all countries from json
     */
    private List<CountryMaster> getAllCountries() {
        try {
            Path file = Paths.get("countries.json");
            return mapper.readValue(file.toFile().getAbsoluteFile(), new TypeReference<List<CountryMaster>>() {
            });
        } catch (IOException e) {
            return new ArrayList<CountryMaster>();
        }
    }

    /**
     * Get countries from json
     */
    protected List<CountryMaster> getCountries() {
        List<CountryMaster> allCountries = getAllCountries();
        List<CountryMaster> countries = new ArrayList<CountryMaster>();
        for (CountryMaster country : allCountries) {
            if (country.getType() != 0) {
                countries.add(country);
            }
        }
        Collections.sort(countries, new Comparator<CountryMaster>() {
            public int compare(CountryMaster o1, CountryMaster o2) {
                if (o1.getType() == o2.getType())
                    return 0;
                return o1.getType() > o2.getType() ? -1 : 1;
            }
        });
        return countries;
    }

    protected AppInformationSolr createAppInformation(ApplicationPlay applicationPlay, String languageCode) {
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
    protected ApplicationPlay getAppInformationByLanguage(String languageCode, String appId, boolean isDaily) throws Exception {
        ApplicationPlay applicationPlay =
                informationApplicationPlayService.getInformationApplications(appId, languageCode);
        StringBuilder path = createAppInformationJSONPath(appId, languageCode, isDaily);
        Files.write(Paths.get(path.toString()), mapper.writeValueAsBytes(applicationPlay));
        return applicationPlay;
    }

    protected StringBuilder createAppInformationJSONPath(String appId, String languageCode, boolean isDaily) {
        StringBuilder path;
        if (isDaily) {
            path = new StringBuilder(CommonUtils.folderBy(rootPath, DataServiceEnum.app_daily.name(), DataTypeEnum.queue.name()).getAbsolutePath());
        } else {
            path = new StringBuilder(CommonUtils.folderBy(rootPath, DataServiceEnum.app_summary.name(), DataTypeEnum.queue.name()).getAbsolutePath());
        }
        path.append("/");
        path.append(languageCode).append(REGEX_3_UNDER_LINE);
        path.append(appId.toLowerCase()).append(JSON_FILE_EXTENSION);
        return path;
    }

    protected void extractAppInformation(String languageCode, String appId, boolean isDaily) throws Exception {
        AppInformationSolr app = appInformationService.findOne(appId + "_" + languageCode);
        if (app == null || isTimeToUpdate(app.getCreateAt())) {
            logger.debug("[Information Store]Get app " + appId + " by language " + languageCode);
            ApplicationPlay applicationPlay = getAppInformationByLanguage(languageCode, appId, isDaily);
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
        }
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
                    AppInformationSolr app = appInformationService.findOne(appId + "_" + languageCode);
                    if (app == null || isTimeToUpdate(app.getCreateAt())) {
                        extractAppInformation(languageCode, appId, isDaily);
                        long delayTime = System.currentTimeMillis() - startTime;
                        CommonUtils.delay(timeGetAppInformation - delayTime);
                    }
                    FileUtils.deleteQuietly(json);
                } catch (GooglePlayRuntimeException ex) {
                    if (ex.getCode() == ExceptionCodes.NETWORK_LIMITED_EXCEPTION) {
                        logger.info("[Information Store][" + appId + "][" + languageCode + "]Error " + ex.getMessage());
                    } else if (ex.getCode() == ExceptionCodes.APP_NOT_FOUND) {
                        moveFile(json.getAbsolutePath(), CommonUtils.folderBy(rootPath, DataServiceEnum.information.name(), DataTypeEnum.not_found.name()).getAbsolutePath());
                        logger.info("[Information Store][" + appId + "][" + languageCode + "]Error " + ex.getMessage());
                    } else {
                        moveFile(json.getAbsolutePath(), CommonUtils.folderBy(rootPath, DataServiceEnum.information.name(), DataTypeEnum.error.name()).getAbsolutePath());
                        if (ex.getCode() == ExceptionCodes.UNKNOWN_EXCEPTION) {
                            logger.error("[Information Store][" + appId + "][" + languageCode + "]Error " + ex.getMessage(), ex);
                        } else {
                            logger.info("[Information Store][" + appId + "][" + languageCode + "]Error " + ex.getMessage());
                        }
                    }
                } catch (Exception ex) {
                    logger.error("[Information Store][" + appId + "][" + languageCode + "]Error " + ex.getMessage(), ex);
                    moveFile(json.getAbsolutePath(), CommonUtils.folderBy(rootPath, DataServiceEnum.information.name(), DataTypeEnum.error.name()).getAbsolutePath());
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
                    AppScreenshotSolr screenshotObject = appScreenshotSolrService.findOne(appId);
                    if (screenshotObject == null || isTimeToUpdate(screenshotObject.getCreateAt())) {
                        // Remove all old screenshot
                        CommonUtils.deleteDirectory(CommonUtils.folderBy(imageStore, ImageTypeEnum.screenshot.name(), appId));
                        ScreenshotPlays screenshotPlays = mapper.readValue(json, ScreenshotPlays.class);
                        List<ScreenshotPlay> screenshotObjects = new ArrayList<ScreenshotPlay>();
                        AppScreenshotMaster appScreenshotMaster = new AppScreenshotMaster();
                        appScreenshotMaster.setAppId(appId);
                        AppScreenshotSolr appScreenshotSolr = new AppScreenshotSolr();
                        appScreenshotSolr.setId(appId);
                        for (String screenshotLink : screenshotPlays.getScreenshots()) {
                            // 5. Create screenshot
                            ScreenshotPlay screenshotPlay = screenshotApplicationPlayService.extractOriginalScreenshot(appId, screenshotLink);
                            screenshotObjects.add(screenshotPlay);
                            appScreenshotSolr.getScreenshotOrigins().add(screenshotPlay.getOriginal());
                            appScreenshotSolr.getScreenshotSources().add(screenshotPlay.getSource());
                            CommonUtils.delay(timeGetAppScreenshot);
                        }
                        appScreenshotMaster.setData(mapper.writeValueAsString(screenshotObjects));
                        appScreenshotMasterRepository.save(appScreenshotMaster);
                        appScreenshotSolrService.save(appScreenshotSolr);
                        moveFile(json.getAbsolutePath(), CommonUtils.folderBy(rootPath, DataServiceEnum.screenshot.name(), DataTypeEnum.log.name()).getAbsolutePath());
                    }
                    FileUtils.deleteQuietly(json);
                } catch (GooglePlayRuntimeException ex) {
                    if (ex.getCode() == ExceptionCodes.NETWORK_LIMITED_EXCEPTION) {
                        logger.info("[Screenshot Store][" + appId + "]Error " + ex.getMessage());
                    } else {
                        moveFile(json.getAbsolutePath(), CommonUtils.folderBy(rootPath, DataServiceEnum.screenshot.name(), DataTypeEnum.error.name()).getAbsolutePath());
                        if (ex.getCode() == ExceptionCodes.UNKNOWN_EXCEPTION) {
                            logger.error("[Screenshot Store][" + appId + "]Error " + ex.getMessage(), ex);
                        } else {
                            logger.info("[Screenshot Store][" + appId + "]Error " + ex.getMessage());
                        }
                    }
                } catch (Exception ex) {
                    logger.error("[Screenshot Store][" + appId + "]Error " + ex.getMessage(), ex);
                    moveFile(json.getAbsolutePath(), CommonUtils.folderBy(rootPath, DataServiceEnum.screenshot.name(), DataTypeEnum.error.name()).getAbsolutePath());
                }
            } else {
                FileUtils.deleteQuietly(json);
            }
        }
        logger.debug("[Screenshot Store]Task end at: " + new Date());
    }
}
