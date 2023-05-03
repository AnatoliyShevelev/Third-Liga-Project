package ru.liga.translator.service;

import org.springframework.stereotype.Service;

import static ru.liga.translator.translation_rules.TranslationRulesMap.I_RULE;
import static ru.liga.translator.translation_rules.TranslationRulesMap.ER_RULE;
import static ru.liga.translator.translation_rules.TranslationRulesMap.FITA_RULE;
import static ru.liga.translator.translation_rules.TranslationRulesMap.YAT_RULE;

@Service
public class TranslateService {

    public String translate(String text) { //todo стоит сделать 4 метода замены и вызывать поочерёдно
        final String[] result = {text}; //todo я понимаю, что идея ругалась, но этот вариант выглядит костыльно)
        I_RULE.keySet().forEach((key) -> result[0] = result[0].replaceAll(key, I_RULE.get(key)));
        ER_RULE.keySet().forEach((key) -> result[0] = result[0].replaceAll(key, ER_RULE.get(key)));
        FITA_RULE.keySet().forEach((key) -> result[0] = result[0].replaceAll(key, FITA_RULE.get(key)));
        YAT_RULE.keySet().forEach((key) -> result[0] = result[0].replaceAll(key, YAT_RULE.get(key)));

        return result[0];
    }
}
