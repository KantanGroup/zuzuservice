package com.zuzuapps.task.app.appstore.services;

import com.zuzuapps.task.app.appstore.common.AppleCategoryEnum;
import com.zuzuapps.task.app.appstore.common.AppleCollectionEnum;
import com.zuzuapps.task.app.appstore.models.SummaryApplicationAppStores;
import com.zuzuapps.task.app.services.CommonService;
import com.zuzuapps.task.app.exceptions.AppStoreRuntimeException;
import com.zuzuapps.task.app.exceptions.ExceptionCodes;
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
public class SummaryApplicationAppStoreService {
    private final Log logger = LogFactory.getLog("SummaryApplicationApplePlayService");

    @Value("${app.summary.site.path:\"http://localhost:5000\"}")
    private String sitePath;

    @Autowired
    private CommonService<SummaryApplicationAppStores> summaryApplicationAppStoresCommonService;

    public SummaryApplicationAppStores getSummaryApplications(AppleCategoryEnum category, AppleCollectionEnum collection, String country, int page) throws AppStoreRuntimeException {
        try {
            StringBuilder url = new StringBuilder(sitePath + "/appstore/apps");
            url = url.append("?start=").append(page);
            url = url.append("&num=100");
            if (category != AppleCategoryEnum.ALL) {
                url = url.append("&category=").append(category.getCategory());
            }
            url = url.append("&collection=").append(collection.getCollection());
            url = url.append("&country=").append(country);
            logger.info("[SummaryApplicationApplePlayService]URL request: " + url.toString());
            ResponseEntity<SummaryApplicationAppStores> responseEntity = summaryApplicationAppStoresCommonService.get(url.toString(), SummaryApplicationAppStores.class);
            if (responseEntity == null) {
                throw new AppStoreRuntimeException(ExceptionCodes.NETWORK_CONNECT_EXCEPTION, "Can't get data from url " + url.toString());
            }
            HttpStatus statusCode = responseEntity.getStatusCode(); // (2)
            if (!statusCode.is2xxSuccessful()) {
                throw new AppStoreRuntimeException(ExceptionCodes.REMOTE_SERVER_EXCEPTION, "Get apps from page " + (page + 1) + " . HTTP Status: " + statusCode.value() + " HTTP Message: " + statusCode.getReasonPhrase());
            }
            SummaryApplicationAppStores applicationPlays = responseEntity.getBody(); // (4)
            if (applicationPlays == null) {
                throw new AppStoreRuntimeException(ExceptionCodes.REMOTE_SERVER_EXCEPTION, "Apps not found");
            } else {
                return applicationPlays;
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
