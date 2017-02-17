package com.zuzuapps.task.app.googleplay;

import com.zuzuapps.task.app.common.CommonUtils;
import com.zuzuapps.task.app.common.DataServiceEnum;
import com.zuzuapps.task.app.common.DataTypeEnum;
import com.zuzuapps.task.app.exceptions.ExceptionCodes;
import com.zuzuapps.task.app.exceptions.GooglePlayRuntimeException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Date;

/**
 * @author tuanta17
 */
@Service
public class AppInformationSummaryService extends AppCommonService {
    final Log logger = LogFactory.getLog("AppInformationSummaryService");

    /**
     * Summary app information update
     */
    public void summaryAppInformationUpdate() {
        while (true) {
            // something that should execute on weekdays only
            String dirPath = CommonUtils.folderBy(rootPath, DataServiceEnum.information_summary.name(), DataTypeEnum.queue.name()).getAbsolutePath();
            File dir = new File(dirPath);
            File[] files = dir.listFiles();
            if (files != null && files.length != 0) {
                try {
                    CommonUtils.sortFilesOrderByTime(files);
                    summaryDailyAppInformation(files);
                } catch (Exception ex) {
                    logger.error("[ProcessError]Error " + ex.getMessage(), ex);
                }
            }
            CommonUtils.delay(timeWaitRuntimeLocal);
        }
    }

    private void summaryDailyAppInformation(File[] files) throws Exception {
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
                    extractAppInformation(languageCode, appId, false);
                    moveFile(json.getAbsolutePath(), CommonUtils.folderBy(rootPath, DataServiceEnum.information_summary.name(), DataTypeEnum.log.name(), time, countryCode).getAbsolutePath());
                    long delayTime = System.currentTimeMillis() - startTime;
                    CommonUtils.delay(timeGetAppInformation - delayTime);
                } catch (GooglePlayRuntimeException ex) {
                    if (ex.getCode() == ExceptionCodes.NETWORK_LIMITED_EXCEPTION) {
                        logger.info("[Information Store][" + appId + "][" + languageCode + "]Error " + ex.getMessage());
                    } else if (ex.getCode() == ExceptionCodes.APP_NOT_FOUND) {
                        moveFile(json.getAbsolutePath(), CommonUtils.folderBy(rootPath, DataServiceEnum.information_summary.name(), DataTypeEnum.not_found.name(), time).getAbsolutePath());
                        logger.info("[Information Store][" + appId + "][" + languageCode + "]Error " + ex.getMessage());
                    } else {
                        moveFile(json.getAbsolutePath(), CommonUtils.folderBy(rootPath, DataServiceEnum.information_summary.name(), DataTypeEnum.error.name(), time).getAbsolutePath());
                        if (ex.getCode() == ExceptionCodes.UNKNOWN_EXCEPTION) {
                            logger.error("[Information Store][" + appId + "][" + languageCode + "]Error " + ex.getMessage(), ex);
                        } else {
                            logger.info("[Information Store][" + appId + "][" + languageCode + "]Error " + ex.getMessage());
                        }
                    }
                } catch (Exception ex) {
                    logger.error("[Information Store][" + appId + "][" + languageCode + "]Error " + ex.getMessage(), ex);
                    moveFile(json.getAbsolutePath(), CommonUtils.folderBy(rootPath, DataServiceEnum.information_summary.name(), DataTypeEnum.error.name(), time).getAbsolutePath());
                }
            } else {
                moveFile(json.getAbsolutePath(), CommonUtils.folderBy(rootPath, DataServiceEnum.information_summary.name(), DataTypeEnum.error.name(), time).getAbsolutePath());
            }
        }
        logger.debug("[Information Store]Task end at: " + new Date());
    }
}
