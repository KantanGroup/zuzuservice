package org.springframework.cloud.task.app.googleplay.servies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * @author tuanta17
 */
public class CommonService<T> {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${google.play.uri:http://localhost:5000}")
    private static String GOOGLE_PLAY_BASE_URI;

    private HttpEntity<String> addHeaders() {
        HttpHeaders headers = new HttpHeaders();
        return new HttpEntity<String>(headers);
    }

    public ResponseEntity<T> get(String url, Class type) {
        return restTemplate.exchange(GOOGLE_PLAY_BASE_URI + url, HttpMethod.GET, addHeaders(), type);
    }

    public ResponseEntity<T> post(String url, Class type) {
        return restTemplate.exchange(GOOGLE_PLAY_BASE_URI + url, HttpMethod.POST, addHeaders(), type);
    }

    public ResponseEntity<T> put(String url, Class type) {
        return restTemplate.exchange(GOOGLE_PLAY_BASE_URI + url, HttpMethod.PUT, addHeaders(), type);
    }

    public ResponseEntity<T> delete(String url, Class type) {
        return restTemplate.exchange(GOOGLE_PLAY_BASE_URI + url, HttpMethod.DELETE, addHeaders(), type);
    }
}