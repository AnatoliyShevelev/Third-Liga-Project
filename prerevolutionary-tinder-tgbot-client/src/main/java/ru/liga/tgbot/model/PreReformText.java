package ru.liga.tgbot.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PreReformText implements Serializable { //todo не нашёл практического применения этого классу, лишь в неиспользуемом методе
    private String text;
}
