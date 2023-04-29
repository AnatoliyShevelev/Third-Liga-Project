package ru.liga.server.dto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.liga.server.person_model.LikedPerson;
import ru.liga.server.person_model.Person;
import ru.liga.server.repository.LikedPersonRepository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PersonMapper {

    private final LikedPersonRepository likedPersonRepository;

    /**
     * Создание представления пользователя со статусом связи
     *
     * @param likedPerson  Идентификатор связанного пользователя
     * @param mainPersonId Идентификатор текщего пользователя
     * @return Представление данных пользователя
     */
    public PersonDto createModel(Person likedPerson, Long mainPersonId) {
        PersonDto personDto = new PersonDto();
        personDto.setId(likedPerson.getId());
        personDto.setPersonId(likedPerson.getPersonId());
        personDto.setFullName(likedPerson.getFullName());
        personDto.setGender(likedPerson.getGender());
        personDto.setGenderSearch(likedPerson.getGenderSearch());
        personDto.setDescription(likedPerson.getDescription());
        personDto.setStatus(getLikedPersonStatus(mainPersonId, likedPerson.getId()));

        log.info("Created person dto: {}", personDto);

        return personDto;
    }

    /**
     * Создание списка представлений пользователей со статусом связи
     *
     * @param likedPersons Список связанных пользователей
     * @param mainPersonId Идентификатор текщего пользователя
     * @return Список представление данных пользователей
     */
    public List<PersonDto> createModelList(List<Person> likedPersons, Long mainPersonId) {
        List<PersonDto> personDtoList = new ArrayList<>();

        for (Person likedPerson : likedPersons) {
            personDtoList.add(createModel(likedPerson, mainPersonId));
        }

        return personDtoList;
    }

    /**
     * Определение статуса связи между пользователями
     *
     * @param mainPersonId  Идентификатор текщего пользователя
     * @param likedPersonId Идентификатор связанного пользователя
     * @return Статус связи между пользователями
     */
    private String getLikedPersonStatus(Long mainPersonId, Long likedPersonId) {
        LikedPerson likePerson = likedPersonRepository.getByMainIdAndLikedId(mainPersonId, likedPersonId);
        LikedPerson likedMePerson = likedPersonRepository.getByMainIdAndLikedId(likedPersonId, mainPersonId);

        if (likePerson != null && likedMePerson != null) {
            return "MATCH";
        } else if (likePerson != null) {
            return "LIKE";
        } else {
            return "LIKED_ME";
        }
    }
}
