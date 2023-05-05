package ru.liga.tgbot.mapper;

import ru.liga.tgbot.dto.PersonDto;
import ru.liga.tgbot.enums.BotState;
import ru.liga.tgbot.enums.Sex;
import ru.liga.tgbot.model.Person;

public class PersonMapper {
    /**
     * Маппер для дозаполнения данных
     *
     * @param person заполняемый объект
     * @param personDTO данные для заполнения
     * @return заполненный объект
     */
    public static Person fillPerson(Person person, PersonDto personDTO) {
        person.setId(personDTO.getId());
        person.setName(personDTO.getFullName());
        person.setDescription(personDTO.getDescription());
        person.setSex(Sex.valueOf(personDTO.getGender()));
        person.setTypeSearch(Sex.valueOf(personDTO.getGenderSearch()));
        person.setBotState(BotState.PROFILE_DONE);
        person.setPageCounter(1);
        return person;
    }

    /**
     * Маппер для заполнения данных
     *
     * @param person заполняемый объект
     * @return заполненный объект
     */
    public static PersonDto fillPersonDto(Person person) {
        PersonDto personDTO = new PersonDto();
        personDTO.setId(person.getId());
        personDTO.setGender(person.getSex().toString());
        personDTO.setPersonId(person.getPersonId());
        personDTO.setFullName(person.getName());
        personDTO.setDescription(person.getDescription());
        personDTO.setGenderSearch(person.getTypeSearch().toString());
        return personDTO;
    }

}
