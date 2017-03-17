package com.zuzuapps.task.app.appstore;

import com.zuzuapps.task.app.AppCommonService;
import com.zuzuapps.task.app.appstore.services.SummaryApplicationAppStoreService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author tuanta17
 */
public class AppleAppCommonService extends AppCommonService {
    protected final Log logger = LogFactory.getLog("AppleAppCommonService");

    protected static final String GZ_FILE_EXTENSION = ".gz";
    protected static final String ZERO_NUMBER = "0";
    protected static final String REGEX_3_UNDER_LINE = "___";
    protected static final String COUNTRY_CODE_DEFAULT = "us";
    protected static final String LANGUAGE_CODE_DEFAULT = "en";

    @Value("${apple.data.root.path:/tmp/appstore}")
    protected String appleRootPath;

    @Value("${apple.data.image.path:/tmp/appstore/imagestore}")
    protected String appleImageStore;

    protected SummaryApplicationAppStoreService summaryApplicationAppStoreService;
}
