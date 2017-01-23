package com.zuzuapps.task.app.googleplay.servies;

import com.zuzuapps.task.app.exceptions.ExceptionCodes;
import com.zuzuapps.task.app.exceptions.GooglePlayRuntimeException;
import com.zuzuapps.task.app.googleplay.models.ApplicationPlay;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * @author tuanta17
 */
@Service
public class InformationApplicationPlayService {
    private final Log logger = LogFactory.getLog("InformationApplicationPlayService");

    @Value("${data.site.path:\"http://localhost:5000\"}")
    private String sitePath;

    @Autowired
    private CommonService<ApplicationPlay> applicationPlaysCommonService;

    public ApplicationPlay getInformationApplications(String appId, String language) throws GooglePlayRuntimeException {
        try {
            StringBuilder url = new StringBuilder(sitePath + "/api/apps");
            url = url.append("/").append(appId);
            url = url.append("/?lang=").append(language);
            logger.info("URL request: " + url.toString());
            ResponseEntity<ApplicationPlay> responseEntity = applicationPlaysCommonService.get(url.toString(), ApplicationPlay.class);
            if (responseEntity == null) {
                throw new GooglePlayRuntimeException(ExceptionCodes.NETWORK_CONNECT_EXCEPTION, "Can't get data from url " + url.toString());
            }
            HttpStatus statusCode = responseEntity.getStatusCode(); // (2)
            if (!statusCode.is2xxSuccessful()) {
                throw new GooglePlayRuntimeException(ExceptionCodes.GOOGLE_PLAY_SERVER_EXCEPTION, "Get app " + appId + " . HTTP Status: " + statusCode.value() + " HTTP Message: " + statusCode.getReasonPhrase());
            }
            HttpHeaders header = responseEntity.getHeaders(); // (3)
            ApplicationPlay applicationPlay = responseEntity.getBody(); // (4)
            if (applicationPlay == null) {
                throw new GooglePlayRuntimeException(ExceptionCodes.GOOGLE_PLAY_SERVER_EXCEPTION, "App " + appId + " not found");
            } else {
                return applicationPlay;
            }
        } catch (GooglePlayRuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new GooglePlayRuntimeException(ExceptionCodes.UNKNOWN_EXCEPTION, e);
        }
    }
}