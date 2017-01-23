package com.zuzuapps.task.app.googleplay;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zuzuapps.task.app.common.CommonUtils;
import com.zuzuapps.task.app.common.DataServiceEnum;
import com.zuzuapps.task.app.common.DataTypeEnum;
import com.zuzuapps.task.app.common.ZipUtil;
import com.zuzuapps.task.app.elasticsearch.repositories.AppIndexElasticSearchRepository;
import com.zuzuapps.task.app.googleplay.models.SummaryApplicationPlay;
import com.zuzuapps.task.app.googleplay.servies.InformationApplicationPlayService;
import com.zuzuapps.task.app.googleplay.servies.SummaryApplicationPlayService;
import com.zuzuapps.task.app.master.repositories.AppIndexMasterRepository;
import com.zuzuapps.task.app.master.repositories.CountryMasterRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

/**
 * @author tuanta17
 */
@Service
public class AppCommonService {
    protected final Log logger = LogFactory.getLog("AppCommonService");
    protected static final String JSON_FILE_EXTENSTION = ".json";
    protected static final String GZ_FILE_EXTENSION = ".gz";
    protected static final String ZERO_NUMBER = "0";
    protected static final String REGEX_3_UNDER_LINE = "___";
    protected static final String COUNTRY_CODE_DEFAULT = "us";
    protected static final String LANGUAGE_CODE_DEFAULT = "en";
    protected final ObjectMapper mapper = new ObjectMapper();

    @Value("${data.root.path:/tmp}")
    protected String rootPath;

    @Value("${time.get.app.info:5000}")
    protected long timeGetAppInfo;

    @Autowired
    protected SummaryApplicationPlayService summaryApplicationPlayService;
    @Autowired
    protected CountryMasterRepository countryRepository;
    @Autowired
    protected AppIndexMasterRepository appIndexMasterRepository;
    @Autowired
    protected AppIndexElasticSearchRepository appIndexElasticSearchRepository;
    @Autowired
    protected InformationApplicationPlayService informationApplicationPlayService;

    protected void queueAppInformation(List<SummaryApplicationPlay> summaryApplicationPlays, String countryCode, String languageCode) {
        for (SummaryApplicationPlay summaryApplicationPlay : summaryApplicationPlays) {
            try {
                StringBuilder path = new StringBuilder(CommonUtils.folderBy(rootPath, DataServiceEnum.information.name(), DataTypeEnum.queue.name(), countryCode).getAbsolutePath());
                path.append("/").append(countryCode).append(REGEX_3_UNDER_LINE);
                path.append(languageCode).append(REGEX_3_UNDER_LINE);
                path.append(summaryApplicationPlay.getAppId().toLowerCase()).append(JSON_FILE_EXTENSTION);
                logger.debug("Write summary of app " + summaryApplicationPlay.getAppId().toLowerCase() + " to queue folder " + path.toString());
                Files.write(Paths.get(path.toString()), mapper.writeValueAsBytes(summaryApplicationPlay));
            } catch (Exception ex) {
                logger.error("Write summary of app error " + ex.getMessage(), ex);
            }
        }
    }

    protected void moveFile(String source, String destination) {
        try {
            Path src = Paths.get(source);
            Path des = Paths.get(destination);
            Path zipFile = Paths.get(src.toFile().getAbsolutePath() + GZ_FILE_EXTENSION);
            logger.debug("Zip json file " + source);
            new ZipUtil().gzip(src.toFile().getAbsolutePath(), zipFile.toFile().getAbsolutePath());
            logger.debug("Move json file " + source + " to log folder " + destination);
            Files.move(zipFile, des.resolve(zipFile.getFileName()), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception ex) {
            logger.warn("Move json file error " + ex.getMessage(), ex);
        }
    }

}
