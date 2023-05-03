package ru.liga.tgbot.service; //todo лучше переложить в отдельный пакет, например, telegram

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.liga.tgbot.cache.PersonCache;
import ru.liga.tgbot.config.BotConfig;

import java.io.IOException;
import java.net.URISyntaxException;


@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {
    @Autowired
    private BotConfig config;
    @Autowired
    private HandlerMessage handlerMessage;
    @Autowired
    private HandlerCallback handlerCallback;


    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            try {
                handleMessage(update);
            } catch (TelegramApiException | IOException | URISyntaxException e) {
                log.error(e.getMessage());
            }
        }
        if (update.hasCallbackQuery()) {
            try {
                handleCallBack(update.getCallbackQuery());
            } catch (IOException | URISyntaxException | TelegramApiException e) {
                log.error(e.getMessage());
            }
        }
    }

    /**
     * Обработка нажатий на кнопку
     *
     * @param callbackQuery колбэк кнопки
     * @throws IOException
     * @throws URISyntaxException
     * @throws TelegramApiException
     */
    private void handleCallBack(CallbackQuery callbackQuery) throws IOException, URISyntaxException, TelegramApiException {
        SendPhoto sendPhoto = handlerCallback.handleSendPhoto(callbackQuery);
        SendMessage sendMessage = handlerCallback.answerCallback(callbackQuery);
        if (sendPhoto != null) {
            execute(sendPhoto);
        } else {
            execute(sendMessage);
        }
    }

    /**
     * Обработка получения сообщений
     *
     * @param update
     * @throws TelegramApiException
     * @throws IOException
     * @throws URISyntaxException
     */
    private void handleMessage(Update update) throws TelegramApiException, IOException, URISyntaxException {
        SendPhoto sendPhoto = handlerMessage.handleSendPhoto(update);
        SendMessage sendMessage = handlerMessage.handleSendMessage(update);
        if (sendPhoto != null) {
            execute(sendPhoto);
        } else {
            execute(sendMessage);
        }
    }
}
