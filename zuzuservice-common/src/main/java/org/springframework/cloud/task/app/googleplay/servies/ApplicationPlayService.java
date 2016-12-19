package org.springframework.cloud.task.app.googleplay.servies;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.task.app.exceptions.ExceptionCodes;
import org.springframework.cloud.task.app.exceptions.GooglePlayRuntimeException;
import org.springframework.cloud.task.app.googleplay.models.ApplicationPlays;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author tuanta17
 */
@Service
public class ApplicationPlayService {
    @Autowired
    RestTemplate restTemplate;

    private final Logger logger = LoggerFactory.getLogger(ApplicationPlayService.class);

    @Autowired
    private CommonService<ApplicationPlays> applicationPlaysCommonService;

    public ApplicationPlays getApplications(int number) throws GooglePlayRuntimeException {
        try {
            String url = "/api/apps/" + number;
            ResponseEntity<ApplicationPlays> responseEntity = applicationPlaysCommonService.get(url, ApplicationPlays.class);
            if (responseEntity == null) {
                throw new GooglePlayRuntimeException(ExceptionCodes.NETWORK_CONNECT_EXCEPTION, "Can't get data from url " + url);
            }
            HttpStatus statusCode = responseEntity.getStatusCode(); // (2)
            if (!statusCode.is2xxSuccessful()) {
                throw new GooglePlayRuntimeException(ExceptionCodes.GOOGLE_PLAY_SERVER_EXCEPTION, "Get apps from number " + number + " . HTTP Status: " + statusCode.value() + " HTTP Message: " + statusCode.getReasonPhrase());
            }
            HttpHeaders header = responseEntity.getHeaders(); // (3)
            ApplicationPlays applicationPlays = responseEntity.getBody(); // (4)
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
