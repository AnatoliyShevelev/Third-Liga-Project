package ru.liga.tgbot.config;

import lombok.Data;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@Data //todo это избыточно
public class RestTemplateConfig { //todo стоит создать бин и инжектить там, где это необходимо
    private RestTemplate restTemplate = new RestTemplate();
}
