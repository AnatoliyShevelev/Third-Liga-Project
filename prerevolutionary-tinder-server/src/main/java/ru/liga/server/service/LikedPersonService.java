package ru.liga.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.liga.server.person_model.LikedPerson;
import ru.liga.server.repository.LikedPersonRepository;
import ru.liga.server.repository.PersonRepository;

@Service
@RequiredArgsConstructor
public class LikedPersonService {
    private final LikedPersonRepository likedPersonRepository;
    private final PersonRepository personRepository;

    /**
     * Создание связи между пользователями
     *
     * @param likedPerson Данные о связи пользователей
     * @return Данные о связи пользователей
     */
    public LikedPerson likePerson(LikedPerson likedPerson) {
        Long mainId = personRepository.findByPersonId(likedPerson.getMainId()).getId();
        Long likedId = personRepository.findByPersonId(likedPerson.getLikedId()).getId();

        LikedPerson likedPersonExists = likedPersonRepository.getByMainIdAndLikedId(mainId, likedId);
        if (likedPersonExists == null) {
            likedPerson.setMainId(mainId);
            likedPerson.setLikedId(likedId);
            return likedPersonRepository.saveAndFlush(likedPerson);
        }

        return likedPersonExists;
    }
}
