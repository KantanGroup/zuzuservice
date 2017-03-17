package com.zuzuapps.task.app.appstore;

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
public class AppleAppInformationDailyService extends AppleAppCommonService {
    final Log logger = LogFactory.getLog("AppleAppInformationDailyService");

    /**
     * Daily app information update
     */
    public void dailyAppInformationUpdate() {
        while (true) {
            // something that should execute on weekdays only
            String dirPath = CommonUtils.folderBy(appleRootPath, DataServiceEnum.information_daily.name(), DataTypeEnum.queue.name()).getAbsolutePath();
            File dir = new File(dirPath);
            File[] files = dir.listFiles();
            if (files != null && files.length != 0) {
                try {
                    CommonUtils.sortFilesOrderByTime(files);
                    processAppInformation(files, true);
                } catch (Exception ex) {
                    logger.error("[ProcessError]Error " + ex.getMessage(), ex);
                }
            }
            CommonUtils.delay(timeWaitRuntimeLocal);
        }
    }
}
