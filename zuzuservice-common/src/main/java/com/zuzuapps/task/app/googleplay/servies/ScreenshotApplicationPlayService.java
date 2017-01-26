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
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;

/**
 * @author tuanta17
 */
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
        return extractOriginalImage(appId, imageLink, ImageTypeEnum.icon.ordinal());
    }

    /**
     * Extract origin screen shoot
     *
     * @param appId     Application id
     * @param imageLink Image link
     * @return
     * @throws GooglePlayRuntimeException
     */
    public ScreenshotPlay extractOriginalScreenshoot(String appId, String imageLink) throws GooglePlayRuntimeException {
        return extractOriginalImage(appId, imageLink, ImageTypeEnum.screenshoot.ordinal());
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
    private ScreenshotPlay extractOriginalImage(String appId, String imageLink, int type) throws GooglePlayRuntimeException {
        try {
            byte[] imageBytes = restTemplate.getForObject(imageLink, byte[].class, addHeaders());
            String appImageOriginPath = appId + "/" + System.currentTimeMillis() + ".png";
            File file = Paths.get(imageStore, appId).toFile();
            if (!file.exists()) {
                file.mkdirs();
            }
            Files.write(Paths.get(imageStore, appImageOriginPath), imageBytes);
            ScreenshotPlay screenshot = new ScreenshotPlay();
            screenshot.setAppId(appId);
            screenshot.setOriginal(appImageOriginPath);
            screenshot.setSource(imageLink);
            screenshot.setType(type);
            return screenshot;
        } catch (IOException ex) {
            throw new GooglePlayRuntimeException(ExceptionCodes.DATA_READ_WRITE_EXCEPTION, ex);
        } catch (Exception ex) {
            throw new GooglePlayRuntimeException(ExceptionCodes.UNKNOWN_EXCEPTION, ex);
        }
    }
}
