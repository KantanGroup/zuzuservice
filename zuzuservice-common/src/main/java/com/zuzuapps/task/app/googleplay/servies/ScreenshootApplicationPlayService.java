package com.zuzuapps.task.app.googleplay.servies;

import com.zuzuapps.task.app.common.ImageTypeEnum;
import com.zuzuapps.task.app.exceptions.ExceptionCodes;
import com.zuzuapps.task.app.exceptions.GooglePlayRuntimeException;
import com.zuzuapps.task.app.googleplay.models.ScreenshootPlay;
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
public class ScreenshootApplicationPlayService {

    @Value("${data.image.path:/tmp")
    private String imageStore;

    @Autowired
    private RestTemplate restTemplate;

    private HttpEntity<String> addHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.put("Upgrade-Insecure-Requests", Collections.singletonList(new String("1")));
        headers.put("User-Agent", Collections.singletonList(new String("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.95 Safari/537.36")));
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
    public ScreenshootPlay extractOriginalIcon(String appId, String imageLink) throws GooglePlayRuntimeException {
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
    public ScreenshootPlay extractOriginalScreenshoot(String appId, String imageLink) throws GooglePlayRuntimeException {
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
    private ScreenshootPlay extractOriginalImage(String appId, String imageLink, int type) throws GooglePlayRuntimeException {
        try {
            byte[] imageBytes = restTemplate.getForObject(imageLink, byte[].class, addHeaders());
            String appImageOriginPath = appId + "/" + System.currentTimeMillis() + ".png";
            File file = Paths.get(imageStore, appId).toFile();
            if (!file.exists()) {
                file.mkdirs();
            }
            Files.write(Paths.get(imageStore, appImageOriginPath), imageBytes);
            ScreenshootPlay screenshoot = new ScreenshootPlay();
            screenshoot.setAppId(appId);
            screenshoot.setOriginal(appImageOriginPath);
            screenshoot.setSource(imageLink);
            screenshoot.setType(type);
            return screenshoot;
        } catch (IOException ex) {
            throw new GooglePlayRuntimeException(ExceptionCodes.DATA_READ_WRITE_EXCEPTION, ex);
        } catch (Exception ex) {
            throw new GooglePlayRuntimeException(ExceptionCodes.UNKNOWN_EXCEPTION, ex);
        }
    }
}
