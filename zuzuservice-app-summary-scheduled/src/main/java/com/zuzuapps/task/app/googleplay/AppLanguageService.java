package com.zuzuapps.task.app.googleplay;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zuzuapps.task.app.common.CommonUtils;
import com.zuzuapps.task.app.googleplay.models.ApplicationPlay;
import com.zuzuapps.task.app.googleplay.servies.InformationApplicationPlayService;
import com.zuzuapps.task.app.master.models.CountryMaster;
import com.zuzuapps.task.app.master.repositories.CountryMasterRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by tuanta on 1/18/17.
 */
@Service
public class AppLanguageService {
    public static final String COUNTRY_CODE_DEFAULT = "us";
    private final ObjectMapper mapper = new ObjectMapper();
    private final Log logger = LogFactory.getLog(ScheduleApplication.class);

    @Value("${data.root.path:/tmp}")
    private String rootPath;
    @Value("${time.get.app.info:5000}")
    private long timeGetAppInfo;
    @Autowired
    private InformationApplicationPlayService informationApplicationPlayService;
    @Autowired
    private CountryMasterRepository countryRepository;

    /**
     * Split app summary to apps
     */
    public void dailyAppInformationUpdate() {
        while (true) {
            // something that should execute on weekdays only
            String dirPath = CommonUtils.queueInformationFolderBy(rootPath, COUNTRY_CODE_DEFAULT);
            File dir = new File(dirPath);
            File[] files = dir.listFiles();
            if (files != null && files.length != 0) {
                queueAppInformation(files);
            }
            CommonUtils.delay(timeGetAppInfo);
        }
    }

    public void queueAppInformation(File[] files) {
        logger.info("[Application Information Store]Cronjob start at: " + new Date());
        Set<String> languages = findDistinctByLanguageCode();
        for (File json : files) {
            String filename = json.getName();
            String[] data = filename.split("___");
            String appId = data[2].replaceAll(".json", "");
            for (String languageCode : languages) {
                try {
                    ApplicationPlay applicationPlay =
                            informationApplicationPlayService.getInformationApplications(appId, languageCode);
                    StringBuilder path = createAppLanguageJSONPath(appId, languageCode);
                    Files.write(Paths.get(path.toString()), mapper.writeValueAsBytes(applicationPlay));
                } catch (Exception ex) {
                    logger.error("[Application Information Store]App language error", ex);
                }
                CommonUtils.delay(timeGetAppInfo);
            }
        }
        logger.info("[Application Information Store]Cronjob end at: " + new Date());
    }

    private StringBuilder createAppLanguageJSONPath(String appId, String languageCode) {
        StringBuilder path = new StringBuilder(CommonUtils.queueAppFolderBy(rootPath, languageCode));
        path.append("/").append(appId.toLowerCase()).append(".json");
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
                String dirPath = CommonUtils.queueAppFolderBy(rootPath, languageCode);
                File dir = new File(dirPath);
                File[] files = dir.listFiles();
                if (files != null && files.length != 0) {
                    queueAppLanguage(files);
                }
            }
            CommonUtils.delay(timeGetAppInfo);
        }
    }

    public void queueAppLanguage(File[] files) {
        logger.info("[Application Language Store]Cronjob start at: " + new Date());
        for (File json : files) {
            // 1. Get data

            // 2. Write data to database

            // 3. Index data

            // 4. Create icon

            // 5. Create screen shoot
        }
        logger.info("[Application Language Store]Cronjob end at: " + new Date());
    }
}
