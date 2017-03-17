package com.zuzuapps.task.app.googlestore.servies;

import com.zuzuapps.task.app.services.CommonService;
import com.zuzuapps.task.app.exceptions.ExceptionCodes;
import com.zuzuapps.task.app.exceptions.GooglePlayRuntimeException;
import com.zuzuapps.task.app.googlestore.common.GoogleCategoryEnum;
import com.zuzuapps.task.app.googlestore.common.GoogleCollectionEnum;
import com.zuzuapps.task.app.googlestore.models.SummaryApplicationGooglePlays;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

/**
 * @author tuanta17
 */
@Service
public class SummaryApplicationGooglePlayService {
    private final Log logger = LogFactory.getLog("SummaryApplicationGooglePlayService");

    @Value("${app.summary.site.path:\"http://localhost:5000\"}")
    private String sitePath;

    @Autowired
    private CommonService<SummaryApplicationGooglePlays> summaryApplicationGooglePlaysCommonService;

    public SummaryApplicationGooglePlays getSummaryApplications(GoogleCategoryEnum category, GoogleCollectionEnum collection, String language, String country, int page) throws GooglePlayRuntimeException {
        try {
            StringBuilder url = new StringBuilder(sitePath + "/googlestore/apps");
            url = url.append("?start=").append(page);
            url = url.append("&num=120");
            if (category != GoogleCategoryEnum.ALL) {
                url = url.append("&category=").append(category.name());
            }
            url = url.append("&collection=").append(collection.name());
            url = url.append("&country=").append(country);
            url = url.append("&lang=").append(language);
            logger.info("[SummaryApplicationGooglePlayService]URL request: " + url.toString());
            ResponseEntity<SummaryApplicationGooglePlays> responseEntity = summaryApplicationGooglePlaysCommonService.get(url.toString(), SummaryApplicationGooglePlays.class);
            if (responseEntity == null) {
                throw new GooglePlayRuntimeException(ExceptionCodes.NETWORK_CONNECT_EXCEPTION, "Can't get data from url " + url.toString());
            }
            HttpStatus statusCode = responseEntity.getStatusCode(); // (2)
            if (!statusCode.is2xxSuccessful()) {
                throw new GooglePlayRuntimeException(ExceptionCodes.REMOTE_SERVER_EXCEPTION, "Get apps from page " + (page + 1) + " . HTTP Status: " + statusCode.value() + " HTTP Message: " + statusCode.getReasonPhrase());
            }
            SummaryApplicationGooglePlays applications = responseEntity.getBody(); // (4)
            if (applications == null) {
                throw new GooglePlayRuntimeException(ExceptionCodes.REMOTE_SERVER_EXCEPTION, "Apps not found");
            } else {
                return applications;
            }
        } catch (ResourceAccessException ex) {
            throw new GooglePlayRuntimeException(ExceptionCodes.NETWORK_CONNECT_EXCEPTION, ex);
        } catch (HttpClientErrorException ex) {
            if (ex.getMessage().contains("400")) {
                throw new GooglePlayRuntimeException(ExceptionCodes.APP_NOT_FOUND, ex);
            } else {
                throw new GooglePlayRuntimeException(ExceptionCodes.NETWORK_LIMITED_EXCEPTION, ex);
            }
        } catch (GooglePlayRuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new GooglePlayRuntimeException(ExceptionCodes.UNKNOWN_EXCEPTION, e);
        }
    }

}
