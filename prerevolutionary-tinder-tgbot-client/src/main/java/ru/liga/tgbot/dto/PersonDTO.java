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
public class PersonDTO { //todo обычно называют просто ..Dto, не капсом

    private Long id;
    private String gender;
    private Long personId;
    private String fullName;
    private String description;
    private String genderSearch;
    private String status;

    public PersonDTO(Person person) { //todo это должно быть в mapper
        this.id = person.getId();
        this.gender = person.getSex().toString();
        this.personId = person.getPersonId();
        this.fullName = person.getName();
        this.description = person.getDescription().toString();
        this.genderSearch = person.getTypeSearch().toString();
    }
}
