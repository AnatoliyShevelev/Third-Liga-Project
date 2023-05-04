package ru.liga.server.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.liga.server.person_model.LikedPerson;
import ru.liga.server.service.LikedPersonService;

@RestController
@RequestMapping("/favorite")
@RequiredArgsConstructor
public class LikedPersonController {
    private final LikedPersonService likedPersonService;
    @PostMapping
    public ResponseEntity<LikedPerson> likePerson(@RequestBody LikedPerson likedPerson) {//DONE todo стоит возвращать ResponseEntity<>
        LikedPerson createdLikedPerson = likedPersonService.likePerson(likedPerson);
        return new ResponseEntity<>(createdLikedPerson, HttpStatus.CREATED);
    }

}