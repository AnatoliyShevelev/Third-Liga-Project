package ru.liga.tgbot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.liga.tgbot.cache.PersonCache;
import ru.liga.tgbot.dto.PersonDTO;
import ru.liga.tgbot.enums.Action;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@Component
public class SenderPhoto {
    @Autowired
    private PersonCache personCache;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private ButtonsMaker buttonsMaker;
    @Value("${path.image}")
    private String filePath;


    /**
     * Получение профиля с кнопками меню
     *
     * @param message  Входящее сообщение
     * @param filePath Путь до файла с картинкой
     * @return Сообщение, готовое для отправки
     * @throws IOException
     * @throws URISyntaxException
     */
    public SendPhoto getMyProfile(Message message, String filePath) throws IOException, URISyntaxException {
        List<List<InlineKeyboardButton>> buttons = buttonsMaker.createButtonsForGetMyProfile();
        InputFile inputFile = getInputFile(filePath);
        return SendPhoto.builder()
                .chatId(message.getChatId().toString())
                .photo(inputFile)
                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                .build();
    }

    /**
     * Получение профиля
     *
     * @param message   Входящее сообщение
     * @param personDTO Профиль
     * @return Сообщение, готовое для отправки
     * @throws IOException
     * @throws URISyntaxException
     */
    public SendPhoto getProfile(Message message, PersonDTO personDTO) throws IOException, URISyntaxException {
        ReplyKeyboardMarkup keyboardMarkup = buttonsMaker.createButtonsForGetProfile();
        InputFile inputFile = getInputFile(getProfileText(personDTO));
        return SendPhoto.builder()
                .chatId(message.getChatId().toString())
                .photo(inputFile)
                .replyMarkup(keyboardMarkup)
                .build();
    }

    /**
     * Получение проифиля в виде картники
     *
     * @param text Текст, размещенный на картинке
     * @return готовый профиль в виде картинки
     * @throws URISyntaxException
     * @throws IOException
     */
    private InputFile getInputFile(String text) throws URISyntaxException, IOException {
        //PreReformText preReformText = profileService.translateToOldSlavonic(text);
        ByteArrayOutputStream byteArrayOutputStream = profileService.profileToPicture(/*preReformText.getText()*/text);
        return new InputFile(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()), "profile");
    }

    /**
     * Получение текста для отоборажения в профиле
     *
     * @param personDTO Профиль
     * @return готовый текст для профиля
     */
    private String getProfileText(PersonDTO personDTO) {
        if (personDTO.getStatus() != null) {
            return personDTO.getFullName() + " - " + Action.valueOf(personDTO.getStatus()).getCaption() + "\n" + personDTO.getDescription();
        } else {
            return personDTO.getFullName() + "\n" + personDTO.getDescription();
        }
    }
}
