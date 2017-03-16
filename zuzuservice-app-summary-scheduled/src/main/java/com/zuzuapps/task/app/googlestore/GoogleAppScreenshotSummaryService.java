package com.zuzuapps.task.app.googlestore;

import com.zuzuapps.task.app.common.CommonUtils;
import com.zuzuapps.task.app.common.DataServiceEnum;
import com.zuzuapps.task.app.common.DataTypeEnum;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * @author tuanta17
 */
@Service
public class GoogleAppScreenshotSummaryService extends GoogleAppCommonService {
    final Log logger = LogFactory.getLog("GoogleAppScreenshotSummaryService");

    /**
     * Split app summary to apps
     */
    public void processAppScreenshotUpdate() {
        while (true) {
            // something that should execute on weekdays only
            String dirPath = CommonUtils.folderBy(googleRootPath, DataServiceEnum.screenshot_summary.name(), DataTypeEnum.queue.name()).getAbsolutePath();
            File dir = new File(dirPath);
            File[] files = dir.listFiles();
            if (files != null && files.length != 0) {
                try {
                    CommonUtils.sortFilesOrderByTime(files);
                    processDailyAppScreenshots(files);
                } catch (Exception ex) {
                    logger.error("[ProcessError]Error " + ex.getMessage(), ex);
                }
            }
            CommonUtils.delay(timeWaitRuntimeLocal);
        }
    }
}
