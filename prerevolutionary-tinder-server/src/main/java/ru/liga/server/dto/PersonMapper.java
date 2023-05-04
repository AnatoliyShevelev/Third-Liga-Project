package ru.liga.server.dto; //todo мапперы в dto? Нужно держать в отдельном пакете - mapper

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.liga.server.model.LikedPerson;
import ru.liga.server.model.Person;
import ru.liga.server.repository.LikedPersonRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PersonMapper { //todo предполагается, что в маппер содержит методы для маппинга entity to dto и обратно,
    // todo сейчас же тут создание модели, списка и определение статуса.. стоит переделать

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
        personDto.setStatus(showLikedPersonStatus(mainPersonId, likedPerson.getId()));

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
    private String showLikedPersonStatus(Long mainPersonId, Long likedPersonId) { //DONE todo не get
        Optional<LikedPerson> likePerson = likedPersonRepository.findByMainIdAndLikedId(mainPersonId, likedPersonId);
        Optional<LikedPerson> likedMePerson = likedPersonRepository.findByMainIdAndLikedId(likedPersonId, mainPersonId);

        if (likePerson.isPresent() && likedMePerson.isPresent()) {
            return "MATCH";
        } else if (likePerson.isPresent()) {
            return "LIKE";
        } else {
            return "LIKED_ME";
        }
    }
}
