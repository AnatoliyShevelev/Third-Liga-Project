package ru.liga.tgbot.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data //todo лучше использовать getter. Data даёт то, что тут не нужно и даже опасно, потому что сейчас поля можно изменить.
public class BotConfig {

    @Value("${bot.name}")
    private String botName;

    @Value("${bot.token}")
    private String token;
}
