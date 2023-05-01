package ru.liga.tgbot.enums;

public enum Action {
    MALE("Сударъ"),
    FEMALE("Сударыня"),
    ALL("Всех"),
    LEFT("Влево"),
    RIGHT("Вправо"),
    SEARCH("Поиск"),
    PROFILE("Анкета"),
    FAVORITES("Любимцы"),
    EDIT("Редактировать"),
    RECIPROCITY("Взаимность"),
    LIKED_ME("Вы понравились"),
    LIKE("Нравится"),
    MENU("Меню");

    private String caption;

    Action(String caption) {
        this.caption = caption;
    }

    public String getCaption() {
        return caption;
    }
}
