package ru.liga.tgbot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.liga.tgbot.cache.PersonCache;
import ru.liga.tgbot.dto.PersonDTO;
import ru.liga.tgbot.enums.Action;
import ru.liga.tgbot.enums.BotState;

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
    private ProfileService profileService; //todo не используется?
    @Autowired
    private ButtonsMaker buttonsMaker; //todo не используется?
    @Autowired
    private PersonService personService;
    @Autowired
    private SenderMessage senderMessage;

    /**
     * Отправление сообщения после отправки сообщения
     *
     * @param update Объект Update
     * @return Сообщение, готовое для отправки
     * @throws IOException
     * @throws URISyntaxException
     */
    public SendMessage handleSendMessage(Update update) throws IOException, URISyntaxException {
        String messageText = update.getMessage().getText();
        Message message = update.getMessage();
        Long userId = message.getFrom().getId();

        log.info("New message from User:{}. userId: {}, chatId: {}, with text: {}",
                message.getFrom().getUserName(),
                userId,
                message.getChatId().toString(),
                message.getText());

        if ("/start".equals(messageText)) {
            PersonDTO person = personService.getPerson(userId);
            if (!personCache.containsKey(userId) && person == null) {
                return getSendMessageQuestionSex(message);
            }
        }
        BotState botState = personCache.getUsersCurrentBotState(userId);
        if (botState.equals(BotState.SET_PROFILE_INFO)) {
            return setProfileInfo(messageText, message, userId);
        }
        return senderMessage.getSendMessage(message.getChatId().toString(), "Команда не поддерживается. Попробуйте повторить еще раз.");
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
            PersonDTO person = personService.getPerson(userId);
            if (person != null) {
                personCache.setPersonCache(userId, person);
                return senderPhoto.getMyProfile(message, person.getFullName() + " " + person.getDescription());
            }
        }

        if (botState.equals(BotState.SEARCH)) {
            if (messageText.equals(Action.RIGHT.getCaption())) {
                personService.likePerson(userId, personCache.getLikedPersonId(userId));
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
     * @throws IOException
     * @throws URISyntaxException
     */
    private SendMessage setProfileInfo(String messageText, Message message, Long userId) throws IOException, URISyntaxException {
        String[] inputDescrTypeFirst = messageText.split("\n");
        String[] inputDescrTypeSecond = messageText.split(" ");
        if (inputDescrTypeFirst.length > 1) {
            return getSendMessageQuestionTypeSearch(messageText, message, userId, "\n");
        }
        if (inputDescrTypeSecond.length > 1) {
            return getSendMessageQuestionTypeSearch(messageText, message, userId, " ");
        } else {
            return senderMessage.getSendMessage(message.getChatId().toString(),
                    "Введите на первой строке - ваше имя, на второй строке описание!, либо все в одной строке!");
        }
    }

    /**
     * Получение итогового профиля с кнопками меню
     *
     * @param message Объект входящего сообщения
     * @param userId  Id текущего пользователя из Телеграмма
     * @return Сообщение, готовое для отправки
     * @throws IOException
     * @throws URISyntaxException
     */
    private SendPhoto getMenuAndProfileWithDescr(Message message, Long userId) throws IOException, URISyntaxException {
        personCache.setNewState(userId, BotState.PROFILE_DONE);
        personCache.resetPagesCounter(userId);
        return senderPhoto.getMyProfile(message, personCache.getNameAndDescription(userId));
    }

    /**
     * Получение следующего профиля для поиска
     *
     * @param message Объект входящего сообщения
     * @param userId  Id текущего пользователя из Телеграмма
     * @return Сообщение, готовое для отправки
     * @throws URISyntaxException
     * @throws IOException
     */
    private SendPhoto getNextLikedProfile(Message message, Long userId) throws URISyntaxException, IOException {
        int pagesCounter = personCache.incrementPagesCounter(userId);
        PersonDTO personDTO = personService.getSuitablePerson(userId, pagesCounter);
        personCache.setLikedPersonId(userId, personDTO.getPersonId());
        return senderPhoto.getProfile(message, personDTO);
    }

    /**
     * Получение следующего профиля для любимцев
     *
     * @param message Объект входящего сообщения
     * @param userId  Id текущего пользователя из Телеграмма
     * @return Сообщение, готовое для отправки
     * @throws URISyntaxException
     * @throws IOException
     */
    private SendPhoto getNextFavoriteProfile(Message message, Long userId) throws URISyntaxException, IOException {
        int pagesCounter = personCache.incrementPagesCounter(userId);
        return senderPhoto.getProfile(message, personService.getFavoritePerson(userId, pagesCounter));
    }

    /**
     * Получение предыдущего профиля для любимцев
     *
     * @param message Объект входящего сообщения
     * @param userId  Id текущего пользователя из Телеграмма
     * @return Сообщение, готовое для отправки
     * @throws URISyntaxException
     * @throws IOException
     */
    private SendPhoto getPrevFavoriteProfile(Message message, Long userId) throws URISyntaxException, IOException {
        int pagesCounter = personCache.minusPagesCounter(userId);
        return senderPhoto.getProfile(message, personService.getFavoritePerson(userId, pagesCounter));
    }

    /**
     * Получение сообщения с вопросом выбора пола для поиска
     *
     * @param messageText Имя с описанием
     * @param message     Объект входящего сообщения
     * @param userId      Id текущего пользователя из Телеграмма
     * @param reg         Регулярное выражение для парсинга
     * @return Сообщение, готовое для отправки
     * @throws IOException
     * @throws URISyntaxException
     */
    //todo не get.. Зачем исключения пробрасываются?
    private SendMessage getSendMessageQuestionTypeSearch(String messageText, Message message, Long userId, String reg) throws IOException, URISyntaxException {
        personCache.setNameAndDescription(messageText, userId, reg);
        personCache.setNewState(userId, BotState.SET_TYPE_SEARCH);
        return senderMessage.getSendMessageQuestionTypeSearch(message);
    }

    /**
     * Получение сообщения с вопросом выбора пола
     *
     * @param message Объект входящего сообщения
     * @return Сообщение, готовое для отправки
     */
    private SendMessage getSendMessageQuestionSex(Message message) { //todo не get..
        personCache.addPersonCache(message.getChat().getId(), BotState.SET_SEX);
        return senderMessage.getSendMessageQuestionSex(message);
    }
}
