package ru.liga.tgbot.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class ProfileServiceConfig {
    @Value("${translate.url}")
    private String translateUrl;
}
