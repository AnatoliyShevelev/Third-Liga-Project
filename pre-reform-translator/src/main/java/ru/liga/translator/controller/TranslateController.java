package ru.liga.translator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.liga.translator.service.TranslateService;

@RestController
@RequestMapping("/translate")
public class TranslateController { //DONE todo в наименовании можно не указывать Rest если других нет - TranslateController

    @Autowired
    private TranslateService translateService;
    @PostMapping
    public ResponseEntity<String> translate(@RequestBody String text) {//todo стоит возвращать ResponseEntity<String>
        String translatedText = translateService.translate(text);
        return new ResponseEntity<>(translatedText, HttpStatus.OK);
    }
}
