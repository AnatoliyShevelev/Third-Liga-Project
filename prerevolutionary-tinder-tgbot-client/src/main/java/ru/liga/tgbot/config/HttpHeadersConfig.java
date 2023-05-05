package ru.liga.tgbot.config;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class HttpHeadersConfig {

    public HttpHeaders settingHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
