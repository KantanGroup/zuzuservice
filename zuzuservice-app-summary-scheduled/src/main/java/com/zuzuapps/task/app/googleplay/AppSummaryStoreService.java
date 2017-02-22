package com.zuzuapps.task.app.googleplay;

import com.zuzuapps.task.app.common.*;
import com.zuzuapps.task.app.googleplay.models.SummaryApplicationPlays;
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
public class AppSummaryStoreService extends AppCommonService {
    final Log logger = LogFactory.getLog("AppSummaryStoreService");

    /**
     * Daily app summary update
     */
    public void summaryAppIndexUpdate() {
        while (true) {
            // something that should execute on weekdays only
            String dirPath = CommonUtils.folderBy(rootPath, DataServiceEnum.top_app_summary.name(), DataTypeEnum.queue.name()).getAbsolutePath();
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
                CategoryEnum category = CategoryEnum.valueOf(data[2].toUpperCase());
                CollectionEnum collection = CollectionEnum.valueOf(data[3]);
                try {
                    SummaryApplicationPlays apps = mapper.readValue(json, SummaryApplicationPlays.class);
                    // Create app info json
                    queueAppInformation(apps.getResults(), countryCode, languageCode, DataServiceEnum.information_summary);
                    // Remove data
                    FileUtils.deleteQuietly(json);
                } catch (Exception ex) {
                    logger.error("[Application Summary --> Information][" + category.name() + "][" + collection.name() + "]Error " + ex.getMessage(), ex);
                    moveFile(json.getAbsolutePath(), CommonUtils.folderBy(rootPath, DataServiceEnum.top_app_summary.name(), DataTypeEnum.error.name(), time).getAbsolutePath());
                }
            } else {
                FileUtils.deleteQuietly(json);
            }
            CommonUtils.delay(5);
        }
        logger.debug("[Application Summary --> Information]Cronjob end at: " + new Date());
    }
}
