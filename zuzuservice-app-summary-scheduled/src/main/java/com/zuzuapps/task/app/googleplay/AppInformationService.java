package com.zuzuapps.task.app.googleplay;

import com.zuzuapps.task.app.common.CommonUtils;
import com.zuzuapps.task.app.common.DataServiceEnum;
import com.zuzuapps.task.app.common.DataTypeEnum;
import com.zuzuapps.task.app.googleplay.models.ApplicationPlay;
import com.zuzuapps.task.app.master.models.CountryMaster;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author tuanta17
 */
@Service
public class AppInformationService extends AppCommonService {
    /**
     * Split app summary to apps
     */
    public void dailyAppInformationUpdate() {
        while (true) {
            // something that should execute on weekdays only
            String dirPath = CommonUtils.folderBy(rootPath, DataServiceEnum.information.name(), DataTypeEnum.queue.name(), COUNTRY_CODE_DEFAULT).getAbsolutePath();
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
                    StringBuilder path = createAppInformationJSONPath(appId, languageCode);
                    Files.write(Paths.get(path.toString()), mapper.writeValueAsBytes(applicationPlay));
                } catch (Exception ex) {
                    logger.error("[Application Information Store]App language error", ex);
                }
                CommonUtils.delay(timeGetAppInfo);
            }
        }
        logger.info("[Application Information Store]Cronjob end at: " + new Date());
    }

    private StringBuilder createAppInformationJSONPath(String appId, String languageCode) {
        StringBuilder path = new StringBuilder(CommonUtils.folderBy(rootPath, DataServiceEnum.app.name(), DataTypeEnum.queue.name()).getAbsolutePath());
        path.append("/");
        path.append(languageCode).append("___");
        path.append(appId.toLowerCase()).append(".json");
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
