package com.zuzuapps.task.app.googlestore.servies;

import com.zuzuapps.task.app.exceptions.ExceptionCodes;
import com.zuzuapps.task.app.exceptions.GooglePlayRuntimeException;
import com.zuzuapps.task.app.googlestore.models.ApplicationGooglePlay;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

/**
 * @author tuanta17
 */
@Service
public class InformationApplicationGooglePlayService {
    private final Log logger = LogFactory.getLog("InformationApplicationPlayService");

    @Value("${app.information.site.path:\"http://localhost:5000\"}")
    private String sitePath;

    @Autowired
    private CommonService<ApplicationGooglePlay> applicationPlaysCommonService;

    public ApplicationGooglePlay getInformationApplications(String appId, String language) throws GooglePlayRuntimeException {
        try {
            StringBuilder url = new StringBuilder(sitePath + "/api/apps");
            url = url.append("/").append(appId);
            url = url.append("/?lang=").append(language);
            logger.info("URL request: " + url.toString());
            ResponseEntity<ApplicationGooglePlay> responseEntity = applicationPlaysCommonService.get(url.toString(), ApplicationGooglePlay.class);
            if (responseEntity == null) {
                throw new GooglePlayRuntimeException(ExceptionCodes.NETWORK_CONNECT_EXCEPTION, "Can't get data from url " + url.toString());
            }
            HttpStatus statusCode = responseEntity.getStatusCode(); // (2)
            if (!statusCode.is2xxSuccessful()) {
                throw new GooglePlayRuntimeException(ExceptionCodes.GOOGLE_PLAY_SERVER_EXCEPTION, "Get app " + appId + " . HTTP Status: " + statusCode.value() + " HTTP Message: " + statusCode.getReasonPhrase());
            }
            HttpHeaders header = responseEntity.getHeaders(); // (3)
            ApplicationGooglePlay applicationPlay = responseEntity.getBody(); // (4)
            if (applicationPlay == null) {
                throw new GooglePlayRuntimeException(ExceptionCodes.GOOGLE_PLAY_SERVER_EXCEPTION, "App " + appId + " not found");
            } else {
                return applicationPlay;
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