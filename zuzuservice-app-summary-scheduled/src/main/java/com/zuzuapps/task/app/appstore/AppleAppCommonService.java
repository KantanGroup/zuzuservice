package com.zuzuapps.task.app.appstore;

import com.zuzuapps.task.app.AppCommonService;
import com.zuzuapps.task.app.appstore.models.SummaryApplicationAppStore;
import com.zuzuapps.task.app.appstore.services.SummaryApplicationAppStoreService;
import com.zuzuapps.task.app.common.CommonUtils;
import com.zuzuapps.task.app.common.DataServiceEnum;
import com.zuzuapps.task.app.common.DataTypeEnum;
import com.zuzuapps.task.app.solr.appstore.models.AppleAppInformationSolr;
import com.zuzuapps.task.app.solr.appstore.repositories.AppleAppIndexSolrRepository;
import com.zuzuapps.task.app.solr.appstore.repositories.AppleAppInformationSolrRepository;
import com.zuzuapps.task.app.solr.appstore.repositories.AppleAppTrendSolrRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author tuanta17
 */
public class AppleAppCommonService extends AppCommonService {
    protected static final String GZ_FILE_EXTENSION = ".gz";
    protected static final String ZERO_NUMBER = "0";
    protected static final String REGEX_3_UNDER_LINE = "___";
    protected static final String COUNTRY_CODE_DEFAULT = "us";
    protected static final String LANGUAGE_CODE_DEFAULT = "en";
    protected final Log logger = LogFactory.getLog("AppleAppCommonService");
    @Value("${apple.data.root.path:/tmp/appstore}")
    protected String appleRootPath;

    @Value("${apple.data.image.path:/tmp/appstore/imagestore}")
    protected String appleImageStore;

    @Autowired
    protected AppleAppIndexSolrRepository appleAppIndexSolrService;
    @Autowired
    protected AppleAppTrendSolrRepository appleAppTrendSolrService;
    @Autowired
    protected AppleAppInformationSolrRepository appleAppInformationSolrService;
    @Autowired
    protected SummaryApplicationAppStoreService summaryApplicationAppStoreService;

    protected void queueAppInformation(List<SummaryApplicationAppStore> summaryApplicationAppStores, String countryCode, String languageCode, DataServiceEnum information) {
        for (SummaryApplicationAppStore summaryApplication : summaryApplicationAppStores) {
            try {
                if (checkAppInformationSolr(summaryApplication.getId() + "_" + languageCode)) {
                    StringBuilder path = new StringBuilder(CommonUtils.folderBy(appleRootPath, information.name(), DataTypeEnum.queue.name()).getAbsolutePath());
                    path.append("/").append(countryCode).append(REGEX_3_UNDER_LINE);
                    path.append(languageCode).append(REGEX_3_UNDER_LINE);
                    path.append(summaryApplication.getAppId()).append(JSON_FILE_EXTENSION);
                    Files.write(Paths.get(path.toString()), mapper.writeValueAsBytes(summaryApplication));
                }
            } catch (Exception ex) {
                logger.error("Write summary of app error " + ex.getMessage(), ex);
            }
        }
    }

    /**
     * Check app information avaiable or not
     */
    private boolean checkAppInformationSolr(String id) {
        AppleAppInformationSolr app = appleAppInformationSolrService.findOne(id);
        if (app == null || isTimeToUpdate(app.getCreateAt())) {
            return true;
        } else {
            CommonUtils.delay(10);
            return false;
        }
    }
}
