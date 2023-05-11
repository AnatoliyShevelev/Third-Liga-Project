package ru.liga.translator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.liga.translator.service.TranslateService;

@RestController
@RequestMapping("/translate")
public class TranslateController {

    @Autowired //todo лучше использовать @RequiredArgsConstructor и сделать поле final
    private TranslateService translateService;
    @PostMapping //todo скорее get чем post, text передать в параметрах запроса
    public ResponseEntity<String> translate(@RequestBody String text) {
        String translatedText = translateService.translate(text);
        return new ResponseEntity<>(translatedText, HttpStatus.OK);
    }
}
