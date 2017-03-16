package com.zuzuapps.task.app.appstore.services;

import com.zuzuapps.task.app.appstore.models.ScreenshotAppStore;
import com.zuzuapps.task.app.common.CommonUtils;
import com.zuzuapps.task.app.common.ImageTypeEnum;
import com.zuzuapps.task.app.exceptions.ExceptionCodes;
import com.zuzuapps.task.app.exceptions.GooglePlayRuntimeException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

/**
 * @author tuanta17
 */
public class ScreenshotApplicationAppStoreService {
    private final Log logger = LogFactory.getLog("ScreenshotApplicationAppStoreService");

    @Value("${apple.data.image.path:/tmp/appstore/imagestore}")
    private String appleImageStore;

    @Autowired
    private RestTemplate restTemplate;

    private HttpEntity<String> addHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.put("Upgrade-Insecure-Requests", Collections.singletonList(new String("1")));
        headers.put("User-Agent", Collections.singletonList(CommonUtils.generateUserAgent()));
        return new HttpEntity<String>(headers);
    }

    /**
     * Extract origin icon
     *
     * @param appId     Application id
     * @param imageLink Image link
     * @return
     * @throws GooglePlayRuntimeException
     */
    public ScreenshotAppStore extractOriginalIcon(String appId, String imageLink) throws GooglePlayRuntimeException {
        return extractOriginalImage(appId, imageLink, ImageTypeEnum.icon);
    }

    /**
     * Extract origin screenshot
     *
     * @param appId     Application id
     * @param imageLink Image link
     * @return
     * @throws GooglePlayRuntimeException
     */
    public ScreenshotAppStore extractOriginalScreenshot(String appId, String imageLink) throws GooglePlayRuntimeException {
        return extractOriginalImage(appId, imageLink, ImageTypeEnum.screenshot);
    }

    /**
     * Extract origin image
     *
     * @param appId     Application id
     * @param imageLink Image link
     * @param type      Image type
     * @return
     * @throws GooglePlayRuntimeException
     */
    private ScreenshotAppStore extractOriginalImage(String appId, String imageLink, ImageTypeEnum type) throws GooglePlayRuntimeException {
        try {
            if (imageLink.startsWith("//")) {
                imageLink = "http:" + imageLink;
            }
            String folderName = type == ImageTypeEnum.screenshot ? ImageTypeEnum.screenshot.name() : ImageTypeEnum.icon.name();
            String appImageOriginPath = appId + (type == ImageTypeEnum.screenshot ? "/" + System.currentTimeMillis() + ".png" : "/icon.png");
            CommonUtils.folderBy(appleImageStore, folderName, appId);
            Path imagePath = Paths.get(appleImageStore, folderName, appImageOriginPath);
            if (!imagePath.toFile().exists()) {
                logger.info("[ScreenshotApplicationGooglePlayService][" + appId + "][" + folderName + "]Extract image from " + imageLink);
                byte[] imageBytes = restTemplate.getForObject(imageLink, byte[].class, addHeaders());
                logger.debug("[ScreenshotApplicationGooglePlayService][" + appId + "][]Write image to " + imagePath.toFile().getAbsolutePath());
                Files.write(imagePath, imageBytes);
            }
            ScreenshotAppStore screenshot = new ScreenshotAppStore();
            screenshot.setAppId(appId);
            screenshot.setOriginal(appImageOriginPath);
            screenshot.setSource(imageLink);
            screenshot.setType((short) type.ordinal());
            return screenshot;
        } catch (IOException ex) {
            throw new GooglePlayRuntimeException(ExceptionCodes.DATA_READ_WRITE_EXCEPTION, ex);
        } catch (HttpClientErrorException ex) {
            if (ex.getMessage().contains("400")) {
                throw new GooglePlayRuntimeException(ExceptionCodes.APP_NOT_FOUND, ex);
            } else {
                throw new GooglePlayRuntimeException(ExceptionCodes.NETWORK_LIMITED_EXCEPTION, ex);
            }
        } catch (ResourceAccessException ex) {
            throw new GooglePlayRuntimeException(ExceptionCodes.NETWORK_CONNECT_EXCEPTION, ex);
        } catch (Exception ex) {
            throw new GooglePlayRuntimeException(ExceptionCodes.UNKNOWN_EXCEPTION, ex);
        }
    }
}
