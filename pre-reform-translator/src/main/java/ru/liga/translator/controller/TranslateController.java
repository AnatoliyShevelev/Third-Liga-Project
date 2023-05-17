package ru.liga.translator.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.liga.translator.service.TranslateService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/translate")
public class TranslateController {

    //DONE todo лучше использовать @RequiredArgsConstructor и сделать поле final
    private final TranslateService translateService;

    @GetMapping//DONE todo скорее get чем post, text передать в параметрах запроса
    public ResponseEntity<String> translate(@RequestParam("text") String text) {
        String translatedText = translateService.translate(text);
        return new ResponseEntity<>(translatedText, HttpStatus.OK);
    }
}
