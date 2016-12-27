package org.springframework.cloud.task.app.googleplay.servies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.task.app.common.CategoryEnum;
import org.springframework.cloud.task.app.common.CollectionEnum;
import org.springframework.cloud.task.app.exceptions.ExceptionCodes;
import org.springframework.cloud.task.app.exceptions.GooglePlayRuntimeException;
import org.springframework.cloud.task.app.googleplay.models.ApplicationPlays;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * @author tuanta17
 */
@Service
public class ApplicationPlayService {
    @Autowired
    private CommonService<ApplicationPlays> applicationPlaysCommonService;

    public ApplicationPlays getApplications(CategoryEnum category, CollectionEnum collection, String language, String country, int page) throws GooglePlayRuntimeException {
        try {
            StringBuilder url = new StringBuilder("/api/apps");
            url = url.append("?start=").append(page);
            url = url.append("&category=").append(category.name());
            url = url.append("&collection=").append(collection.name());
            url = url.append("&country=").append(country);
            url = url.append("&lang=").append(language);
            ResponseEntity<ApplicationPlays> responseEntity = applicationPlaysCommonService.get(url.toString(), ApplicationPlays.class);
            if (responseEntity == null) {
                throw new GooglePlayRuntimeException(ExceptionCodes.NETWORK_CONNECT_EXCEPTION, "Can't get data from url " + url.toString());
            }
            HttpStatus statusCode = responseEntity.getStatusCode(); // (2)
            if (!statusCode.is2xxSuccessful()) {
                throw new GooglePlayRuntimeException(ExceptionCodes.GOOGLE_PLAY_SERVER_EXCEPTION, "Get apps from page " + (page + 1) + " . HTTP Status: " + statusCode.value() + " HTTP Message: " + statusCode.getReasonPhrase());
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
