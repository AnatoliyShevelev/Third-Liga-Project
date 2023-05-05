package ru.liga.tgbot.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import ru.liga.tgbot.enums.BotState;
import ru.liga.tgbot.enums.Sex;

@Data
@AllArgsConstructor
@Builder
@ToString
public class Person {
    private Long id;
    private Long personId;
    private Sex sex;
    private String name;
    private String description;
    private Sex typeSearch;
    private BotState botState;
    private int pageCounter;
    private int pages;
    private Long likedPersonId;

}
