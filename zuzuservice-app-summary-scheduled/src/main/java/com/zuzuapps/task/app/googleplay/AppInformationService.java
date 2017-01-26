package com.zuzuapps.task.app.googleplay;

import com.zuzuapps.task.app.common.CommonUtils;
import com.zuzuapps.task.app.common.DataServiceEnum;
import com.zuzuapps.task.app.common.DataTypeEnum;
import com.zuzuapps.task.app.googleplay.models.ApplicationPlay;
import com.zuzuapps.task.app.master.models.CountryMaster;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
    final Log logger = LogFactory.getLog("AppInformationService");

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
            CommonUtils.delay(timeWaitRuntimeLocal);
        }
    }

    private void queueAppInformation(File[] files) {
        logger.debug("[Application Information Store]Cronjob start at: " + new Date());
        String time = CommonUtils.getDailyByTime();
        for (File json : files) {
            logger.debug("[Application Information Store]File " + json.getAbsolutePath());
            String filename = json.getName();
            String[] data = filename.split(REGEX_3_UNDER_LINE);
            if (data.length >= 2) {
                String appId = data[2].replaceAll(JSON_FILE_EXTENSTION, "");
                try {
                    ApplicationPlay applicationPlay =
                            informationApplicationPlayService.getInformationApplications(appId, LANGUAGE_CODE_DEFAULT);
                    StringBuilder path = createAppInformationJSONPath(appId, LANGUAGE_CODE_DEFAULT);
                    Files.write(Paths.get(path.toString()), mapper.writeValueAsBytes(applicationPlay));
                } catch (Exception ex) {
                    logger.error("[Application Information Store][" + appId + "][" + LANGUAGE_CODE_DEFAULT + "]Error " + ex.getMessage(), ex);
                }
                CommonUtils.delay(timeGetAppInformation);
                moveFile(json.getAbsolutePath(), CommonUtils.folderBy(rootPath, DataServiceEnum.information.name(), DataTypeEnum.log.name(), time, COUNTRY_CODE_DEFAULT).getAbsolutePath());
            } else {
                moveFile(json.getAbsolutePath(), CommonUtils.folderBy(rootPath, DataServiceEnum.information.name(), DataTypeEnum.error.name(), time).getAbsolutePath());
            }
        }
        logger.debug("[Application Information Store]Cronjob end at: " + new Date());
    }

    private StringBuilder createAppInformationJSONPath(String appId, String languageCode) {
        StringBuilder path = new StringBuilder(CommonUtils.folderBy(rootPath, DataServiceEnum.app.name(), DataTypeEnum.queue.name()).getAbsolutePath());
        path.append("/");
        path.append(languageCode).append(REGEX_3_UNDER_LINE);
        path.append(appId.toLowerCase()).append(JSON_FILE_EXTENSTION);
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
