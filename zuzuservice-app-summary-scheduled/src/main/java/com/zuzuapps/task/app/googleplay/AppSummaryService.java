package com.zuzuapps.task.app.googleplay;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zuzuapps.task.app.common.CategoryEnum;
import com.zuzuapps.task.app.common.CollectionEnum;
import com.zuzuapps.task.app.common.CommonUtils;
import com.zuzuapps.task.app.elasticsearch.repositories.AppIndexElasticSearchRepository;
import com.zuzuapps.task.app.googleplay.models.SummaryApplicationPlays;
import com.zuzuapps.task.app.googleplay.servies.SummaryApplicationPlayService;
import com.zuzuapps.task.app.master.repositories.AppIndexMasterRepository;
import com.zuzuapps.task.app.master.repositories.CountryMasterRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

/**
 * @author tuanta17
 */
@Service
public class AppSummaryService {
    public static final String COUNTRY_CODE_DEFAULT = "us";
    public static final String LANGUAGE_CODE_DEFAULT = "en";
    private final ObjectMapper mapper = new ObjectMapper();
    private final Log logger = LogFactory.getLog(ScheduleApplication.class);

    @Value("${data.root.path:/tmp}")
    private String rootPath;
    @Value("${time.get.app.info:5000}")
    private long timeGetAppInfo;
    @Autowired
    private SummaryApplicationPlayService summaryApplicationPlayService;
    @Autowired
    private CountryMasterRepository countryRepository;
    @Autowired
    private AppIndexMasterRepository appIndexMasterRepository;
    @Autowired
    private AppIndexElasticSearchRepository appIndexElasticSearchRepository;

    /**
     * Get all in USA
     */
    public void appSummary() {
        logger.info("[Application Summary]Cronjob start at: " + new Date());
        // something that should execute on weekdays only
        String time = CommonUtils.getDailyByTime();
        for (CollectionEnum collection : CollectionEnum.values()) {
            for (CategoryEnum category : CategoryEnum.values()) {
                int page = 1;
                while (true) {
                    try {
                        SummaryApplicationPlays summaryApplicationPlays
                                = summaryApplicationPlayService.getSummaryApplications(category, collection, LANGUAGE_CODE_DEFAULT, COUNTRY_CODE_DEFAULT, page);
                        StringBuilder path = queueAppSummaryJSONPath(time, collection, category, page);
                        logger.debug("[Application Summary]Write app summary to json " + path.toString());
                        Files.write(Paths.get(path.toString()), mapper.writeValueAsBytes(summaryApplicationPlays));
                        if (summaryApplicationPlays.getResults().size() < 120) {
                            break;
                        }
                    } catch (Exception ex) {
                        logger.error(ex);
                        break;
                    }
                    page++;
                    CommonUtils.delay(timeGetAppInfo);
                }
            }
        }
        logger.info("[Application Summary]Cronjob end at: " + new Date());
    }

    private StringBuilder queueAppSummaryJSONPath(String time, CollectionEnum collection, CategoryEnum category, int page) {
        StringBuilder path = new StringBuilder(CommonUtils.queueSummaryFolderBy(rootPath, time));
        path.append("/").append(COUNTRY_CODE_DEFAULT).append("___");
        path.append(category.name().toLowerCase()).append("___");
        path.append(collection.name().toLowerCase()).append("___");
        path.append(time).append("___").append(page).append(".json");
        return path;
    }


    /**
     * Split app summary to apps
     */
    public void queueAppInformation() {

    }

}
