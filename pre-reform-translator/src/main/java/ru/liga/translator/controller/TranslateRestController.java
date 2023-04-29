package ru.liga.translator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.liga.translator.service.TranslateService;

@RestController
@RequestMapping("/translate")
public class TranslateRestController {

    @Autowired
    private TranslateService translateService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public String translate(@RequestBody String text) {
        return translateService.translate(text);
    }
}
