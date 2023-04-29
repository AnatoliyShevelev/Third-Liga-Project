package ru.liga.server.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.liga.server.person_model.LikedPerson;
import ru.liga.server.service.LikedPersonService;

@RestController
@RequestMapping("/favorite")
@RequiredArgsConstructor
public class LikedPersonController {
    private final LikedPersonService likedPersonService;

    /**
     * Запрос на создание связи между пользователями
     *
     * @param likedPerson Данные о свзи пользователей
     * @return Данные о связи пользователей
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LikedPerson likePerson(@RequestBody LikedPerson likedPerson) {
        return likedPersonService.likePerson(likedPerson);
    }
}