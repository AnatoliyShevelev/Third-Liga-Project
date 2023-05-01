package ru.liga.tgbot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.liga.tgbot.cache.PersonCache;

import java.util.List;

@Component
public class SenderMessage {
    @Autowired
    private ButtonsMaker buttonsMaker;
    @Autowired
    private PersonCache personCache;

    /**
     * Сообщение для вопроса пола
     *
     * @param message Входящее сообщение
     * @return Сообщение, готовое для отправки
     */
    public SendMessage getSendMessageQuestionSex(Message message) {
        List<List<InlineKeyboardButton>> buttons = buttonsMaker.createButtonsForQuestionSex();
        return SendMessage.builder()
                .chatId(message.getChatId().toString())
                .text("Вы сударь иль сударыня?")
                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                .build();
    }

    /**
     * Сообщение для вопроса пола для поиска
     *
     * @param message Входящее сообщение
     * @return Сообщение, готовое для отправки
     */
    public SendMessage getSendMessageQuestionTypeSearch(Message message) {
        List<List<InlineKeyboardButton>> buttons = buttonsMaker.createButtonsForQuestionTypeSearch();
        return SendMessage.builder()
                .chatId(message.getChatId().toString())
                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                .text("Выберете, кого будем искать:").build();
    }

    /**
     * Сообщение после выбора пола
     *
     * @param chatId Id чата для отправки
     * @param sex    выбранный пол
     * @return Сообщение, готовое для отправки
     */
    public SendMessage getSendSuccessSetSex(String chatId, String sex) {
        return SendMessage.builder().chatId(chatId).text("Поздравляю, " + sex + ", теперь введите описание").build();
    }

    /**
     * Сообщение с передаваемым текстом
     *
     * @param chatId Id чата для отправки
     * @param text   текст сообщения
     * @return Сообщение, готовое для отправки
     */
    public SendMessage getSendMessage(String chatId, String text) {
        return SendMessage.builder().chatId(chatId).text(text).build();
    }
}
