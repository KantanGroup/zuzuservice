package com.zuzuapps.task.app.googleplay;

import com.zuzuapps.task.app.common.CommonUtils;
import com.zuzuapps.task.app.common.DataServiceEnum;
import com.zuzuapps.task.app.common.DataTypeEnum;
import com.zuzuapps.task.app.exceptions.ExceptionCodes;
import com.zuzuapps.task.app.exceptions.GooglePlayRuntimeException;
import com.zuzuapps.task.app.googleplay.models.ScreenshotPlay;
import com.zuzuapps.task.app.googleplay.models.ScreenshotPlays;
import com.zuzuapps.task.app.master.models.AppScreenshotMaster;
import com.zuzuapps.task.app.solr.models.AppScreenshotSolr;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author tuanta17
 */
@Service
public class AppScreenshotService extends AppCommonService {
    final Log logger = LogFactory.getLog("AppScreenshotService");

    /**
     * Split app summary to apps
     */
    public void dailyAppScreenshotUpdate() {
        while (true) {
            // something that should execute on weekdays only
            String dirPath = CommonUtils.folderBy(rootPath, DataServiceEnum.screenshoot.name(), DataTypeEnum.queue.name()).getAbsolutePath();
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

    private void processDailyAppScreenshots(File[] files) throws Exception {
        logger.debug("[Screenshot Store]Task start at: " + new Date());
        for (File json : files) {
            logger.info("[Screenshot Store]File " + json.getAbsolutePath());
            String filename = json.getName();
            String[] data = filename.split(REGEX_3_UNDER_LINE);
            if (data.length >= 2) {
                String appId = data[0];
                try {
                    AppScreenshotSolr screenshotObject = appScreenshotSolrService.findOne(appId);
                    if (screenshotObject == null) {
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
                    }
                    moveFile(json.getAbsolutePath(), CommonUtils.folderBy(rootPath, DataServiceEnum.screenshoot.name(), DataTypeEnum.log.name(), CommonUtils.getDailyByTime()).getAbsolutePath());
                } catch (GooglePlayRuntimeException ex) {
                    if (ex.getCode() == ExceptionCodes.NETWORK_LIMITED_EXCEPTION) {
                        logger.info("[Screenshot Store][" + appId + "]Error " + ex.getMessage());
                    } else {
                        moveFile(json.getAbsolutePath(), CommonUtils.folderBy(rootPath, DataServiceEnum.screenshoot.name(), DataTypeEnum.error.name(), CommonUtils.getDailyByTime()).getAbsolutePath());
                        if (ex.getCode() == ExceptionCodes.UNKNOWN_EXCEPTION) {
                            logger.error("[Screenshot Store][" + appId + "]Error " + ex.getMessage(), ex);
                        } else {
                            logger.info("[Screenshot Store][" + appId + "]Error " + ex.getMessage());
                        }
                    }
                } catch (Exception ex) {
                    logger.error("[Screenshot Store][" + appId + "]Error " + ex.getMessage(), ex);
                    moveFile(json.getAbsolutePath(), CommonUtils.folderBy(rootPath, DataServiceEnum.screenshoot.name(), DataTypeEnum.error.name(), CommonUtils.getDailyByTime()).getAbsolutePath());
                }
            } else {
                moveFile(json.getAbsolutePath(), CommonUtils.folderBy(rootPath, DataServiceEnum.screenshoot.name(), DataTypeEnum.error.name(), CommonUtils.getDailyByTime()).getAbsolutePath());
            }
        }
        logger.debug("[Screenshot Store]Task end at: " + new Date());
    }
}
