package ru.liga.tgbot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.liga.tgbot.cache.PersonCache;
import ru.liga.tgbot.dto.PersonDTO;
import ru.liga.tgbot.enums.BotState;
import ru.liga.tgbot.enums.Sex;

import java.io.IOException;
import java.net.URISyntaxException;

@Slf4j
@Component
public class HandlerCallback {
    @Autowired
    private PersonCache personCache;
    @Autowired
    private PersonService personService;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private SenderPhoto senderPhoto;
    @Autowired
    private SenderMessage senderMessage;

    /**
     * Отправление сообщения после нажатия на кнопку
     *
     * @param callbackQuery колбек нажатия на кнопку
     * @return Итоговое сообщение
     * @throws URISyntaxException
     */
    public SendMessage answerCallback(CallbackQuery callbackQuery) throws URISyntaxException {

        Message message = callbackQuery.getMessage();
        String[] param = callbackQuery.getData().split(":");
        Long userId = callbackQuery.getFrom().getId();
        BotState botState = personCache.getUsersCurrentBotState(userId);

        if (botState.equals(BotState.SET_SEX)) {
            personCache.setNewState(userId, BotState.SET_PROFILE_INFO);
            personCache.setNewSex(userId, Sex.valueOf(param[0]));
            return senderMessage.getSendSuccessSetSex(message.getChatId().toString(), param[1]);
        }
        if (botState.equals(BotState.EDIT)) {
            personCache.setNewState(userId, BotState.SET_SEX);
            return senderMessage.getSendMessageQuestionSex(message);
        }
        if (botState.equals(BotState.FAVORITES) && personCache.getPages(userId) == 0) {
            personCache.setNewState(userId, BotState.PROFILE_DONE);
            return senderMessage.getSendMessage(message.getChatId().toString(), "Пока нет любимцев");
        }
        return senderMessage.getSendMessage(message.getChatId().toString(), "Команда не поддерживается. Попробуйте повторить еще раз.");
    }

    /**
     * Отправление Фото после нажатия на кнопку
     *
     * @param callbackQuery колбек нажатия на кнопку
     * @return Итоговое фото
     * @throws IOException
     * @throws URISyntaxException
     */
    public SendPhoto handleSendPhoto(CallbackQuery callbackQuery) throws IOException, URISyntaxException {
        Message message = callbackQuery.getMessage();
        String[] param = callbackQuery.getData().split(":");
        Long userId = callbackQuery.getFrom().getId();
        BotState botState = personCache.getUsersCurrentBotState(userId);

        if (botState.equals(BotState.SET_TYPE_SEARCH)) {
            return setTypeSearch(message, param[0], userId);
        }

        if (botState.equals(BotState.PROFILE_DONE)) {
            BotState newBotState;
            if (param[0].equals(Sex.ALL.toString()) || param[0].equals(Sex.MALE.toString()) || param[0].equals(Sex.FEMALE.toString())) {
                newBotState = BotState.SEARCH;
            }
            else {
                newBotState = BotState.valueOf(param[0]);
            }
            personCache.setNewState(userId, newBotState);
            if (newBotState.equals(BotState.SEARCH)) {
                return startSearching(message, userId);
            }
            if (newBotState.equals(BotState.FAVORITES)) {
                int pagesCounter = personService.getCountFavoritePerson(userId);
                setPagesCache(userId, pagesCounter);
                if (pagesCounter > 0) {
                    PersonDTO personDTO = personService.getFavoritePerson(userId, 1);
                    return senderPhoto.getProfile(message, personDTO);
                }
            }
        }
        return null;
    }

    /**
     * Поиск подходящих анкет
     *
     * @param message Входящее сообщение
     * @param userId  Id текущего пользователя из Телеграмма
     * @return Фото с кнопками готовое для отправки
     * @throws URISyntaxException
     * @throws IOException
     */
    private SendPhoto startSearching(Message message, Long userId) throws URISyntaxException, IOException {
        setPagesCache(userId, personService.getCountSuitablePerson(userId));
        PersonDTO personDTO = personService.getSuitablePerson(userId, 1);
        personCache.setLikedPersonId(userId, personDTO.getPersonId());
        return senderPhoto.getProfile(message, personDTO);
    }

    /**
     * Установления пола для поиска и отправка готовой анкеты
     *
     * @param message    Входящее сообщение
     * @param typeSearch пол для поиска
     * @param userId     Id текущего пользователя из Телеграмма
     * @return Сообщение с готовым профилем и кнопками
     * @throws URISyntaxException
     * @throws IOException
     */
    private SendPhoto setTypeSearch(Message message, String typeSearch, Long userId) throws URISyntaxException, IOException {
        personCache.setNewState(userId, BotState.PROFILE_DONE);
        personCache.setTypeSearch(userId, Sex.valueOf(typeSearch));
        personService.createPerson(personCache.getUsersCurrentPerson(userId));
        String text = personCache.getNameAndDescription(userId);
        return senderPhoto.getMyProfile(message, text);
    }

    /**
     * Устновелния кеша, связанного с страницами
     *
     * @param userId       Id текущего пользователя из Телеграмма
     * @param pagesCounter Кол-во страниц
     */
    private void setPagesCache(Long userId, int pagesCounter) {
        personCache.setPages(userId, pagesCounter);
        personCache.resetPagesCounter(userId);
    }
}
