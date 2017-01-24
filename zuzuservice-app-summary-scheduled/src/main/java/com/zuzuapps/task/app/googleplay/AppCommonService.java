package com.zuzuapps.task.app.googleplay;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zuzuapps.task.app.common.CommonUtils;
import com.zuzuapps.task.app.common.DataServiceEnum;
import com.zuzuapps.task.app.common.DataTypeEnum;
import com.zuzuapps.task.app.common.ZipUtil;
import com.zuzuapps.task.app.elasticsearch.repositories.AppIndexElasticSearchRepository;
import com.zuzuapps.task.app.elasticsearch.repositories.AppTrendElasticSearchRepository;
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
    protected static final String JSON_FILE_EXTENSTION = ".json";
    protected static final String GZ_FILE_EXTENSION = ".gz";
    protected static final String ZERO_NUMBER = "0";
    protected static final String REGEX_3_UNDER_LINE = "___";
    protected static final String COUNTRY_CODE_DEFAULT = "us";
    protected static final String LANGUAGE_CODE_DEFAULT = "en";
    protected final Log logger = LogFactory.getLog("AppCommonService");
    protected final ObjectMapper mapper = new ObjectMapper();

    @Value("${data.root.path:/tmp}")
    protected String rootPath;

    @Value("${time.get.app.information:2000}")
    protected long timeGetAppInformation;

    @Value("${time.get.app.summary:4000}")
    protected long timeGetAppSummary;

    @Value("${time.wait.runtime.local:200}")
    protected long timeWaitRuntimeLocal;

    @Autowired
    protected SummaryApplicationPlayService summaryApplicationPlayService;
    @Autowired
    protected CountryMasterRepository countryRepository;
    @Autowired
    protected AppIndexMasterRepository appIndexMasterRepository;
    @Autowired
    protected AppIndexElasticSearchRepository appIndexElasticSearchRepository;
    @Autowired
    protected AppTrendElasticSearchRepository appTrendElasticSearchRepository;
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
            logger.debug("Move json file " + source + " to log folder " + destination);
            Files.move(src, des.resolve(src.getFileName()), StandardCopyOption.REPLACE_EXISTING);
            Path inputFile = Paths.get(des.toFile().getAbsolutePath(), src.getFileName().toString());
            Path zipFile = Paths.get(des.toFile().getAbsolutePath(), src.getFileName() + GZ_FILE_EXTENSION);
            logger.debug("Zip json file " + inputFile + " to file " + zipFile);
            new ZipUtil().gzip(inputFile.toFile().getAbsolutePath(), zipFile.toFile().getAbsolutePath());
            logger.debug("Remove json file " + inputFile);
            Files.delete(inputFile);
        } catch (Exception ex) {
            logger.warn("Move json file error " + ex.getMessage(), ex);
        }
    }

}
