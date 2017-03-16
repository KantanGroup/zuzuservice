package com.zuzuapps.task.app.googlestore;

import com.zuzuapps.task.app.common.*;
import com.zuzuapps.task.app.googlestore.models.SummaryApplicationGooglePlays;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Date;

/**
 * @author tuanta17
 */
@Service
public class GoogleAppSummaryStoreService extends GoogleAppCommonService {
    final Log logger = LogFactory.getLog("GoogleAppSummaryStoreService");

    /**
     * Daily app summary update
     */
    public void summaryAppIndexUpdate() {
        while (true) {
            // something that should execute on weekdays only
            String dirPath = CommonUtils.folderBy(googleRootPath, DataServiceEnum.top_app_summary.name(), DataTypeEnum.queue.name()).getAbsolutePath();
            File dir = new File(dirPath);
            File[] files = dir.listFiles();
            if (files != null && files.length != 0) {
                try {
                    CommonUtils.sortFilesOrderByTime(files);
                    processAppSummary(files);
                } catch (Exception ex) {
                    logger.error("[ProcessError]Error " + ex.getMessage(), ex);
                }
            }
            CommonUtils.delay(timeWaitRuntimeLocal);
        }
    }

    private void processAppSummary(File[] files) throws Exception {
        logger.debug("[Application Summary --> Information]Cronjob end at: " + new Date());
        // something that should execute on weekdays only
        String time = CommonUtils.getDailyByTime();
        for (File json : files) {
            logger.info("[Application Summary --> Information]File " + json.getAbsolutePath());
            String filename = json.getName();
            String[] data = filename.split(REGEX_3_UNDER_LINE);
            if (data.length >= 4) {
                String countryCode = data[0];
                String languageCode = data[1];
                GooogleCategoryEnum category = GooogleCategoryEnum.valueOf(data[2].toUpperCase());
                GoogleCollectionEnum collection = GoogleCollectionEnum.valueOf(data[3]);
                try {
                    SummaryApplicationGooglePlays apps = mapper.readValue(json, SummaryApplicationGooglePlays.class);
                    // Create app info json
                    queueAppInformation(apps.getResults(), countryCode, languageCode, DataServiceEnum.information_summary);
                    // Remove data
                    FileUtils.deleteQuietly(json);
                } catch (Exception ex) {
                    logger.error("[Application Summary --> Information][" + category.name() + "][" + collection.name() + "]Error " + ex.getMessage(), ex);
                    moveFile(json.getAbsolutePath(), CommonUtils.folderBy(googleRootPath, DataServiceEnum.top_app_summary.name(), DataTypeEnum.error.name(), time).getAbsolutePath());
                }
            } else {
                FileUtils.deleteQuietly(json);
            }
            CommonUtils.delay(5);
        }
        logger.debug("[Application Summary --> Information]Cronjob end at: " + new Date());
    }
}
