package com.zuzuapps.task.app.googleplay;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zuzuapps.task.app.common.CommonUtils;
import com.zuzuapps.task.app.common.DataServiceEnum;
import com.zuzuapps.task.app.common.DataTypeEnum;
import com.zuzuapps.task.app.common.GZipUtil;
import com.zuzuapps.task.app.googleplay.models.ApplicationPlay;
import com.zuzuapps.task.app.googleplay.models.ScreenshotPlay;
import com.zuzuapps.task.app.googleplay.models.SummaryApplicationPlay;
import com.zuzuapps.task.app.googleplay.servies.InformationApplicationPlayService;
import com.zuzuapps.task.app.googleplay.servies.ScreenshotApplicationPlayService;
import com.zuzuapps.task.app.googleplay.servies.SummaryApplicationPlayService;
import com.zuzuapps.task.app.master.models.AppScreenshotMaster;
import com.zuzuapps.task.app.master.models.CountryMaster;
import com.zuzuapps.task.app.master.repositories.AppIndexMasterRepository;
import com.zuzuapps.task.app.master.repositories.AppLanguageMasterRepository;
import com.zuzuapps.task.app.master.repositories.AppMasterRepository;
import com.zuzuapps.task.app.master.repositories.AppScreenshotMasterRepository;
import com.zuzuapps.task.app.solr.models.AppInformationSolr;
import com.zuzuapps.task.app.solr.repositories.AppIndexSolrRepository;
import com.zuzuapps.task.app.solr.repositories.AppInformationSolrRepository;
import com.zuzuapps.task.app.solr.repositories.AppScreenshotSolrRepository;
import com.zuzuapps.task.app.solr.repositories.AppTrendSolrRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
    protected AppIndexMasterRepository appIndexMasterRepository;
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

    protected void queueAppInformation(List<SummaryApplicationPlay> summaryApplicationPlays, String countryCode, String languageCode, boolean isDaily) {
        if (isDaily) {
            queueDailyInformation(summaryApplicationPlays, countryCode, languageCode);
        } else {
            queueSummaryInformation(summaryApplicationPlays, countryCode, languageCode);
        }
    }

    private void queueSummaryInformation(List<SummaryApplicationPlay> summaryApplicationPlays, String countryCode, String languageCode) {
        for (SummaryApplicationPlay summaryApplicationPlay : summaryApplicationPlays) {
            try {
                StringBuilder path = new StringBuilder(CommonUtils.folderBy(rootPath, DataServiceEnum.information_summary.name(), DataTypeEnum.queue.name()).getAbsolutePath());
                path.append("/").append(countryCode).append(REGEX_3_UNDER_LINE);
                path.append(languageCode).append(REGEX_3_UNDER_LINE);
                path.append(summaryApplicationPlay.getAppId().toLowerCase()).append(JSON_FILE_EXTENSION);
                logger.debug("Write summary of app " + summaryApplicationPlay.getAppId().toLowerCase() + " to queue folder " + path.toString());
                Files.write(Paths.get(path.toString()), mapper.writeValueAsBytes(summaryApplicationPlay));
            } catch (Exception ex) {
                logger.error("Write summary of app error " + ex.getMessage(), ex);
            }
        }
    }

    private void queueDailyInformation(List<SummaryApplicationPlay> summaryApplicationPlays, String countryCode, String languageCode) {
        for (SummaryApplicationPlay summaryApplicationPlay : summaryApplicationPlays) {
            try {
                StringBuilder path = new StringBuilder(CommonUtils.folderBy(rootPath, DataServiceEnum.information_daily.name(), DataTypeEnum.queue.name()).getAbsolutePath());
                path.append("/").append(countryCode).append(REGEX_3_UNDER_LINE);
                path.append(languageCode).append(REGEX_3_UNDER_LINE);
                path.append(summaryApplicationPlay.getAppId().toLowerCase()).append(JSON_FILE_EXTENSION);
                logger.debug("Write daily of app " + summaryApplicationPlay.getAppId().toLowerCase() + " to queue folder " + path.toString());
                Files.write(Paths.get(path.toString()), mapper.writeValueAsBytes(summaryApplicationPlay));
            } catch (Exception ex) {
                logger.error("Write daily of app error " + ex.getMessage(), ex);
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
            logger.warn("Move json file error " + ex.getMessage(), ex);
        }
    }

    protected AppScreenshotMaster createAppScreenshotMaster(ScreenshotPlay screenshotPlay) {
        AppScreenshotMaster app = new AppScreenshotMaster();
        app.setAppId(screenshotPlay.getAppId());
        app.setSource(screenshotPlay.getSource());
        app.setType((short) screenshotPlay.getType());
        app.setOriginal(screenshotPlay.getOriginal());
        return app;
    }

    /**
     * Get distinct language
     */
    protected Set<String> findDistinctByLanguageCode() {
        List<CountryMaster> countries = getCountries();
        Set<String> languages = new HashSet<String>();
        for (CountryMaster country : countries) {
            if (country.getType() != 0) {
                languages.add(country.getLanguageCode());
            }
        }
        return languages;
    }

    /**
     * Time to update
     *
     * @param appTime App time
     */
    protected boolean isTimeToUpdate(Date appTime) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -timeUpdateAppInformation);
        String appTime_ = CommonUtils.getTimeBy(appTime, "yyyyMMdd");
        String currentTime_ = CommonUtils.getTimeBy(cal.getTime(), "yyyyMMdd");
        return appTime_.equalsIgnoreCase(currentTime_);
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
        Collections.sort(countries, new Comparator<CountryMaster>(){
            public int compare(CountryMaster o1, CountryMaster o2){
                if(o1.getType() == o2.getType())
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
        StringBuilder path = createAppInformationJSONPath(appId, languageCode, isDaily);;
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
}
