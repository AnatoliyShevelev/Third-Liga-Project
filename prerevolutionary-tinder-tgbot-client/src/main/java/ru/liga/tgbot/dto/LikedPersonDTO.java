package ru.liga.tgbot.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LikedPersonDTO { //todo обычно называют просто ..Dto, не капсом
    private Long mainId;
    private Long likedId;
}
