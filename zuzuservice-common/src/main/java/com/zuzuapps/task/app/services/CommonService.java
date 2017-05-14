package com.zuzuapps.task.app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author tuanta17
 */
@Service
public class CommonService<T> {

    @Autowired
    private RestTemplate restTemplate;

    private HttpHeaders headers = new HttpHeaders();


    private HttpEntity<String> addHeaders() {
        return new HttpEntity<String>(headers);
    }

    public void addHeader(String headerName, String headerValue) {
        if (headers == null) {
            headers = new HttpHeaders();
        }
        headers.add(headerName, headerValue);
    }

    public ResponseEntity<T> get(String url, Class type) throws Exception {
        return restTemplate.exchange(url, HttpMethod.GET, addHeaders(), type);
    }

    public ResponseEntity<T> post(String url, Class type) throws Exception {
        return restTemplate.exchange(url, HttpMethod.POST, addHeaders(), type);
    }

    public ResponseEntity<T> put(String url, Class type) throws Exception {
        return restTemplate.exchange(url, HttpMethod.PUT, addHeaders(), type);
    }

    public ResponseEntity<T> delete(String url, Class type) throws Exception {
        return restTemplate.exchange(url, HttpMethod.DELETE, addHeaders(), type);
    }
}