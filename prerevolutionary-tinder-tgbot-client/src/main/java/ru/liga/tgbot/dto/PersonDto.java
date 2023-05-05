package ru.liga.tgbot.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.liga.tgbot.model.Person;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class PersonDto {

    private Long id;
    private String gender;
    private Long personId;
    private String fullName;
    private String description;
    private String genderSearch;
    private String status;

}
