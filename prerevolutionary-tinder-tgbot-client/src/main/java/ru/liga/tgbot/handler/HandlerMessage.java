package ru.liga.tgbot.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.liga.tgbot.cache.PersonCache;
import ru.liga.tgbot.dto.PersonDto;
import ru.liga.tgbot.enums.Action;
import ru.liga.tgbot.enums.BotState;
import ru.liga.tgbot.service.PersonAdapterService;
import ru.liga.tgbot.service.SenderMessage;
import ru.liga.tgbot.service.SenderPhoto;

import java.io.IOException;
import java.net.URISyntaxException;

@Slf4j
@Component
public class HandlerMessage {

    @Autowired
    private PersonCache personCache;
    @Autowired
    private SenderPhoto senderPhoto;
    @Autowired
    private PersonAdapterService personAdapterService;
    @Autowired
    private SenderMessage senderMessage;

    /**
     * Отправление сообщения после отправки сообщения
     *
     * @param update Объект Update
     * @return Сообщение, готовое для отправки
     * @throws URISyntaxException
     */
    public SendMessage handleSendMessage(Update update) throws URISyntaxException {
        String messageText = update.getMessage().getText();
        Message message = update.getMessage();
        Long userId = message.getFrom().getId();

        log.info("New message from User:{}. userId: {}, chatId: {}, with text: {}",
                message.getFrom().getUserName(),
                userId,
                message.getChatId().toString(),
                message.getText());

        if ("/start".equals(messageText)) {
            PersonDto person = personAdapterService.findPerson(userId);
            if (!personCache.containsKey(userId) && person == null) {
                return receiveSendMessageQuestionSex(message);
            }
        }
        BotState botState = personCache.getUsersCurrentBotState(userId);
        if (botState.equals(BotState.SET_PROFILE_INFO)) {
            return setProfileInfo(messageText, message, userId);
        }
        return senderMessage.sendTextMessage(message.getChatId().toString(), "Команда не поддерживается. Попробуйте повторить еще раз.");
    }

    /**
     * Отправление фото после отправки сообщения
     *
     * @param update Объект Update
     * @return Сообщение, готовое для отправки
     * @throws IOException
     * @throws URISyntaxException
     */
    public SendPhoto handleSendPhoto(Update update) throws IOException, URISyntaxException {
        String messageText = update.getMessage().getText();
        Message message = update.getMessage();
        Long userId = message.getFrom().getId();
        BotState botState = personCache.getUsersCurrentBotState(userId);
        if (botState.equals(BotState.DEF)) {
            PersonDto person = personAdapterService.findPerson(userId);
            if (person != null) {
                personCache.fillPersonCache(userId, person);
                return senderPhoto.receiveMyProfile(message, person.getFullName() + " " + person.getDescription());
            }
        }

        if (botState.equals(BotState.SEARCH)) {
            if (messageText.equals(Action.RIGHT.getCaption())) {
                personAdapterService.likePerson(userId, personCache.getLikedPersonId(userId));
                return getNextLikedProfile(message, userId);
            }
            if (messageText.equals(Action.LEFT.getCaption())) {
                return getNextLikedProfile(message, userId);
            }
            if (messageText.equals(Action.MENU.getCaption())) {
                return getMenuAndProfileWithDescr(message, userId);
            }
        }
        if (botState.equals(BotState.FAVORITES)) {
            if (messageText.equals(Action.RIGHT.getCaption())) {
                return getNextFavoriteProfile(message, userId);
            }
            if (messageText.equals(Action.MENU.getCaption())) {
                return getMenuAndProfileWithDescr(message, userId);
            }
            if (messageText.equals(Action.LEFT.getCaption())) {
                return getPrevFavoriteProfile(message, userId);
            }
        }

        return null;
    }

    /**
     * Установление имени и описания профиля
     *
     * @param messageText Текст сообщения
     * @param message     Объект входящего сообщения
     * @param userId      Id текущего пользователя из Телеграмма
     * @return Сообщение, готовое для отправки
     */
    private SendMessage setProfileInfo(String messageText, Message message, Long userId) {
        String[] inputDescrTypeFirst = messageText.split("\n");
        String[] inputDescrTypeSecond = messageText.split(" ");
        if (inputDescrTypeFirst.length > 1) {
            return receiveSendMessageQuestionTypeSearch(messageText, message, userId, "\n");
        }
        if (inputDescrTypeSecond.length > 1) {
            return receiveSendMessageQuestionTypeSearch(messageText, message, userId, " ");
        } else {
            return senderMessage.sendTextMessage(message.getChatId().toString(),
                    "Введите на первой строке - ваше имя, на второй строке описание!, либо все в одной строке!");
        }
    }

    /**
     * Получение итогового профиля с кнопками меню
     *
     * @param message Объект входящего сообщения
     * @param userId  Id текущего пользователя из Телеграмма
     * @return Сообщение, готовое для отправки
     */
    private SendPhoto getMenuAndProfileWithDescr(Message message, Long userId) {
        personCache.setNewState(userId, BotState.PROFILE_DONE);
        personCache.resetPagesCounter(userId);
        return senderPhoto.receiveMyProfile(message, personCache.getNameAndDescription(userId));
    }

    /**
     * Получение следующего профиля для поиска
     *
     * @param message Объект входящего сообщения
     * @param userId  Id текущего пользователя из Телеграмма
     * @return Сообщение, готовое для отправки
     * @throws URISyntaxException
     */
    private SendPhoto getNextLikedProfile(Message message, Long userId) throws URISyntaxException {
        int pagesCounter = personCache.incrementPagesCounter(userId);
        PersonDto personDTO = personAdapterService.findSuitablePerson(userId, pagesCounter);
        personCache.setLikedPersonId(userId, personDTO.getPersonId());
        return senderPhoto.receiveProfile(message, personDTO);
    }

    /**
     * Получение следующего профиля для любимцев
     *
     * @param message Объект входящего сообщения
     * @param userId  Id текущего пользователя из Телеграмма
     * @return Сообщение, готовое для отправки
     * @throws URISyntaxException
     */
    private SendPhoto getNextFavoriteProfile(Message message, Long userId) throws URISyntaxException {
        int pagesCounter = personCache.incrementPagesCounter(userId);
        return senderPhoto.receiveProfile(message, personAdapterService.findFavoritePerson(userId, pagesCounter));
    }

    /**
     * Получение предыдущего профиля для любимцев
     *
     * @param message Объект входящего сообщения
     * @param userId  Id текущего пользователя из Телеграмма
     * @return Сообщение, готовое для отправки
     * @throws URISyntaxException
     */
    private SendPhoto getPrevFavoriteProfile(Message message, Long userId) throws URISyntaxException {
        int pagesCounter = personCache.minusPagesCounter(userId);
        return senderPhoto.receiveProfile(message, personAdapterService.findFavoritePerson(userId, pagesCounter));
    }

    /**
     * Получение сообщения с вопросом выбора пола для поиска
     *
     * @param messageText Имя с описанием
     * @param message     Объект входящего сообщения
     * @param userId      Id текущего пользователя из Телеграмма
     * @param reg         Регулярное выражение для парсинга
     * @return Сообщение, готовое для отправки
     */
    private SendMessage receiveSendMessageQuestionTypeSearch(String messageText, Message message, Long userId, String reg) {
        personCache.setNameAndDescription(messageText, userId, reg);
        personCache.setNewState(userId, BotState.SET_TYPE_SEARCH);
        return senderMessage.sendMessageQuestionTypeSearch(message);
    }

    /**
     * Получение сообщения с вопросом выбора пола
     *
     * @param message Объект входящего сообщения
     * @return Сообщение, готовое для отправки
     */
    private SendMessage receiveSendMessageQuestionSex(Message message) {
        personCache.addPersonCache(message.getChat().getId(), BotState.SET_SEX);
        return senderMessage.sendMessageQuestionSex(message);
    }
}
