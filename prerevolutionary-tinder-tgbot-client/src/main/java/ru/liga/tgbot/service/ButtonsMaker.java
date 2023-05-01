package ru.liga.tgbot.service;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.liga.tgbot.enums.Action;
import ru.liga.tgbot.enums.BotState;
import ru.liga.tgbot.enums.Sex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class ButtonsMaker {
    /**
     * Создание кнопок для вопроса, связанным с полом
     *
     * @return  Лист кнопок
     */
    public List<List<InlineKeyboardButton>> createButtonsForQuestionSex() {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        buttons.add(Arrays.asList(
                InlineKeyboardButton.builder()
                        .text(Action.MALE.getCaption())
                        .callbackData(Sex.MALE + ":" + Action.MALE.getCaption())
                        .build(),
                InlineKeyboardButton.builder()
                        .text(Action.FEMALE.getCaption())
                        .callbackData(Sex.FEMALE + ":" + Action.FEMALE.getCaption())
                        .build()));
        return buttons;
    }

    /**
     * Создание кнопок для вопроса выбора пола
     *
     * @return  Лист кнопок
     */
    public List<List<InlineKeyboardButton>> createButtonsForQuestionTypeSearch() {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        buttons.add(Arrays.asList(
                InlineKeyboardButton.builder()
                        .text(Action.MALE.getCaption())
                        .callbackData(Sex.MALE + ":" + Action.MALE.getCaption())
                        .build(),
                InlineKeyboardButton.builder()
                        .text(Action.FEMALE.getCaption())
                        .callbackData(Sex.FEMALE + ":" + Action.FEMALE.getCaption())
                        .build(),
                InlineKeyboardButton.builder()
                        .text(Action.ALL.getCaption())
                        .callbackData(Sex.ALL + ":" + Action.ALL.getCaption())
                        .build()));
        return buttons;
    }

    /**
     * Создание кнопок для показа своего профиля
     *
     * @return  Лист кнопок
     */
    public List<List<InlineKeyboardButton>> createButtonsForGetMyProfile() {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        buttons.add(Arrays.asList(
                InlineKeyboardButton.builder()
                        .text(Action.SEARCH.getCaption())
                        .callbackData(BotState.SEARCH.toString())
                        .build(),
                InlineKeyboardButton.builder()
                        .text(Action.EDIT.getCaption())
                        .callbackData(BotState.EDIT.toString())
                        .build(),
                InlineKeyboardButton.builder()
                        .text(Action.FAVORITES.getCaption())
                        .callbackData(BotState.FAVORITES.toString())
                        .build()));
        return buttons;
    }

    /**
     * Создание кнопок влево/меню/аправо
     *
     * @return  Готовая клавиатура
     */
    public ReplyKeyboardMarkup createButtonsForGetProfile() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();

        row.add(Action.LEFT.getCaption());
        row.add(Action.MENU.getCaption());
        row.add(Action.RIGHT.getCaption());

        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;
    }
}
