package ru.liga.server.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.liga.server.model.LikedPerson;
import ru.liga.server.service.LikedPersonService;

import java.util.Optional;

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
    public ResponseEntity<LikedPerson> likePerson(@RequestBody LikedPerson likedPerson) {
        Optional<LikedPerson> createdLikedPerson = likedPersonService.likePerson(likedPerson);
        return createdLikedPerson.map(person -> new ResponseEntity<>(person, HttpStatus.CREATED))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }
}
