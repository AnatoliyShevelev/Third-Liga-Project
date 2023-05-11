package ru.liga.translator.service;

import org.springframework.stereotype.Service;

import static ru.liga.translator.translation_rules.TranslationRulesMap.I_RULE;
import static ru.liga.translator.translation_rules.TranslationRulesMap.ER_RULE;
import static ru.liga.translator.translation_rules.TranslationRulesMap.FITA_RULE;
import static ru.liga.translator.translation_rules.TranslationRulesMap.YAT_RULE;

@Service
public class TranslateService { //todo где тесты..?

    public String translate(String text) { //todo переделать в return replaceWithIRule(replaceWithERRule(re... text);
        text = replaceWithIRule(text);
        text = replaceWithERRule(text);
        text = replaceWithFitaRule(text);
        text = replaceWithYatRule(text);
        return text;
    }

    private String replaceWithIRule(String text) {
        for (String key : I_RULE.keySet()) {
            text = text.replaceAll(key, I_RULE.get(key));
        }
        return text;
    }

    private String replaceWithERRule(String text) {
        for (String key : ER_RULE.keySet()) {
            text = text.replaceAll(key, ER_RULE.get(key));
        }
        return text;
    }

    private String replaceWithFitaRule(String text) {
        for (String key : FITA_RULE.keySet()) {
            text = text.replaceAll(key, FITA_RULE.get(key));
        }
        return text;
    }

    private String replaceWithYatRule(String text) {
        for (String key : YAT_RULE.keySet()) {
            text = text.replaceAll(key, YAT_RULE.get(key));
        }
        return text;
    }
}

