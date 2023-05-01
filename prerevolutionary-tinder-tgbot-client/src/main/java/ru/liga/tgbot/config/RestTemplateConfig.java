package ru.liga.tgbot.config;

import lombok.Data;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@Data
public class RestTemplateConfig {
    private RestTemplate restTemplate = new RestTemplate();
}
