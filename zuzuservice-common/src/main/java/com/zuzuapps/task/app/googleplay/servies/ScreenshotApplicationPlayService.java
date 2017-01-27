package com.zuzuapps.task.app.googleplay.servies;

import com.zuzuapps.task.app.common.CommonUtils;
import com.zuzuapps.task.app.common.ImageTypeEnum;
import com.zuzuapps.task.app.exceptions.ExceptionCodes;
import com.zuzuapps.task.app.exceptions.GooglePlayRuntimeException;
import com.zuzuapps.task.app.googleplay.models.ScreenshotPlay;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;

/**
 * @author tuanta17
 */
@Service
public class ScreenshotApplicationPlayService {
    private final Log logger = LogFactory.getLog("ScreenshotApplicationPlayService");

    @Value("${data.image.path:/tmp")
    private String imageStore;

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
    public ScreenshotPlay extractOriginalIcon(String appId, String imageLink) throws GooglePlayRuntimeException {
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
    public ScreenshotPlay extractOriginalScreenshot(String appId, String imageLink) throws GooglePlayRuntimeException {
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
    private ScreenshotPlay extractOriginalImage(String appId, String imageLink, ImageTypeEnum type) throws GooglePlayRuntimeException {
        logger.debug("[ScreenshotApplicationPlayService][" + appId + "]Extract image from " + imageLink);
        try {
            if (imageLink.startsWith("//")) {
                imageLink = "http:" + imageLink;
            }
            byte[] imageBytes = restTemplate.getForObject(imageLink, byte[].class, addHeaders());
            String appImageOriginPath = appId + (type == ImageTypeEnum.screenshot ? "/" + System.currentTimeMillis() + ".png" : "/icon.png");
            CommonUtils.folderBy(imageStore, appId);
            logger.debug("[ScreenshotApplicationPlayService][" + appId + "]Write image from " + imageStore);
            Files.write(Paths.get(imageStore, appImageOriginPath), imageBytes);
            ScreenshotPlay screenshot = new ScreenshotPlay();
            screenshot.setAppId(appId);
            screenshot.setOriginal(appImageOriginPath);
            screenshot.setSource(imageLink);
            screenshot.setType((short) type.ordinal());
            return screenshot;
        } catch (IOException ex) {
            throw new GooglePlayRuntimeException(ExceptionCodes.DATA_READ_WRITE_EXCEPTION, ex);
        } catch (ResourceAccessException ex) {
            throw new GooglePlayRuntimeException(ExceptionCodes.NETWORK_CONNECT_EXCEPTION, ex);
        } catch (Exception ex) {
            throw new GooglePlayRuntimeException(ExceptionCodes.UNKNOWN_EXCEPTION, ex);
        }
    }
}
