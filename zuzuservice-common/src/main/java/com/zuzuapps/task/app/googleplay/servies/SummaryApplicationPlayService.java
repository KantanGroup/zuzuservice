package com.zuzuapps.task.app.googleplay.servies;

import com.zuzuapps.task.app.common.CategoryEnum;
import com.zuzuapps.task.app.common.CollectionEnum;
import com.zuzuapps.task.app.exceptions.ExceptionCodes;
import com.zuzuapps.task.app.exceptions.GooglePlayRuntimeException;
import com.zuzuapps.task.app.googleplay.models.SummaryApplicationPlays;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * @author tuanta17
 */
@Service
public class SummaryApplicationPlayService {
    private final Log logger = LogFactory.getLog("SummaryApplicationPlayService");

    @Value("${app.summary.site.path:\"http://localhost:5000\"}")
    private String sitePath;

    @Autowired
    private CommonService<SummaryApplicationPlays> applicationPlaysCommonService;

    public SummaryApplicationPlays getSummaryApplications(CategoryEnum category, CollectionEnum collection, String language, String country, int page) throws GooglePlayRuntimeException {
        try {
            StringBuilder url = new StringBuilder(sitePath + "/api/apps");
            url = url.append("?start=").append(page);
            url = url.append("&num=120");
            url = url.append("&category=").append(category.name());
            url = url.append("&collection=").append(collection.name());
            url = url.append("&country=").append(country);
            url = url.append("&lang=").append(language);
            logger.info("[Summary Application]URL request: " + url.toString());
            ResponseEntity<SummaryApplicationPlays> responseEntity = applicationPlaysCommonService.get(url.toString(), SummaryApplicationPlays.class);
            if (responseEntity == null) {
                throw new GooglePlayRuntimeException(ExceptionCodes.NETWORK_CONNECT_EXCEPTION, "Can't get data from url " + url.toString());
            }
            HttpStatus statusCode = responseEntity.getStatusCode(); // (2)
            if (!statusCode.is2xxSuccessful()) {
                throw new GooglePlayRuntimeException(ExceptionCodes.GOOGLE_PLAY_SERVER_EXCEPTION, "Get apps from page " + (page + 1) + " . HTTP Status: " + statusCode.value() + " HTTP Message: " + statusCode.getReasonPhrase());
            }
            SummaryApplicationPlays applicationPlays = responseEntity.getBody(); // (4)
            if (applicationPlays == null) {
                throw new GooglePlayRuntimeException(ExceptionCodes.GOOGLE_PLAY_SERVER_EXCEPTION, "Apps not found");
            } else {
                return applicationPlays;
            }
        } catch (GooglePlayRuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new GooglePlayRuntimeException(ExceptionCodes.UNKNOWN_EXCEPTION, e);
        }
    }

}
