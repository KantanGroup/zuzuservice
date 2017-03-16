package com.zuzuapps.task.app.appstore.services;

import com.zuzuapps.task.app.appstore.models.ApplicationAppStore;
import com.zuzuapps.task.app.common.CommonService;
import com.zuzuapps.task.app.exceptions.AppStoreRuntimeException;
import com.zuzuapps.task.app.exceptions.ExceptionCodes;
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
public class InformationApplicationAppStoreService {
    private final Log logger = LogFactory.getLog("InformationApplicationAppStoreService");

    @Value("${app.information.site.path:\"http://localhost:5000\"}")
    private String sitePath;

    @Autowired
    private CommonService<ApplicationAppStore> applicationAppStoreCommonService;

    public ApplicationAppStore getInformationApplications(String appId, String countryCode) throws AppStoreRuntimeException {
        try {
            StringBuilder url = new StringBuilder(sitePath + "/appstore/apps");
            url = url.append("/").append(appId);
            url = url.append("/?country=").append(countryCode);
            logger.info("URL request: " + url.toString());
            ResponseEntity<ApplicationAppStore> responseEntity = applicationAppStoreCommonService.get(url.toString(), ApplicationAppStore.class);
            if (responseEntity == null) {
                throw new AppStoreRuntimeException(ExceptionCodes.NETWORK_CONNECT_EXCEPTION, "Can't get data from url " + url.toString());
            }
            HttpStatus statusCode = responseEntity.getStatusCode(); // (2)
            if (!statusCode.is2xxSuccessful()) {
                throw new AppStoreRuntimeException(ExceptionCodes.REMOTE_SERVER_EXCEPTION, "Get app " + appId + " . HTTP Status: " + statusCode.value() + " HTTP Message: " + statusCode.getReasonPhrase());
            }
            HttpHeaders header = responseEntity.getHeaders(); // (3)
            ApplicationAppStore application = responseEntity.getBody(); // (4)
            if (application == null) {
                throw new AppStoreRuntimeException(ExceptionCodes.REMOTE_SERVER_EXCEPTION, "App " + appId + " not found");
            } else {
                return application;
            }
        } catch (ResourceAccessException ex) {
            throw new AppStoreRuntimeException(ExceptionCodes.NETWORK_CONNECT_EXCEPTION, ex);
        } catch (HttpClientErrorException ex) {
            if (ex.getMessage().contains("400")) {
                throw new AppStoreRuntimeException(ExceptionCodes.APP_NOT_FOUND, ex);
            } else {
                throw new AppStoreRuntimeException(ExceptionCodes.NETWORK_LIMITED_EXCEPTION, ex);
            }
        } catch (AppStoreRuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new AppStoreRuntimeException(ExceptionCodes.UNKNOWN_EXCEPTION, e);
        }
    }
}