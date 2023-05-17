package ru.liga.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.liga.server.model.LikedPerson;
import ru.liga.server.repository.LikedPersonRepository;
import ru.liga.server.repository.PersonRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikedPersonService { //todo где тесты..?
    private final LikedPersonRepository likedPersonRepository;
    private final PersonRepository personRepository;

    /**
     * Создание связи между пользователями
     *
     * @param likedPerson Данные о связи пользователей
     * @return Данные о связи пользователей
     */
    public Optional<LikedPerson> likePerson(LikedPerson likedPerson) {
        Long mainId = personRepository.findByPersonId(likedPerson.getMainId()).orElseThrow().getId();
        Long likedId = personRepository.findByPersonId(likedPerson.getLikedId()).orElseThrow().getId();

        Optional<LikedPerson> likedPersonExists = likedPersonRepository.findByMainIdAndLikedId(mainId, likedId);
        if (likedPersonExists.isEmpty()) {
            likedPerson.setMainId(mainId);
            likedPerson.setLikedId(likedId);
            return Optional.of(likedPersonRepository.saveAndFlush(likedPerson));
        }

        return likedPersonExists;
    }
}
