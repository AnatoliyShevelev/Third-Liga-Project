package ru.liga.server.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.liga.server.dto.PersonDto;
import ru.liga.server.person_model.Person;
import ru.liga.server.service.PersonService;

import java.util.List;

@RestController
@RequestMapping("/persons")
@RequiredArgsConstructor
public class PersonRestController { //todo в наименовании можно не указывать Rest если других нет - TranslateController
    //todo относится ко всем эндпоинтам - стоит возвращать ResponseEntity<>
    private final PersonService personService;

    /**
     * Запрос данных всех пользователей
     *
     * @return Список данных пользователей
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Person> findAllPersons() {
        return personService.findAll();
    }

    /**
     * Запрос данных пользователя по идентификатору
     *
     * @param personId Идентификатор текущего пользователя
     * @return Данные пользователя
     */
    @GetMapping("/{personId}")
    @ResponseStatus(HttpStatus.OK)
    public Person findPerson(@PathVariable Long personId) {
        return personService.findByPersonId(personId);
    }

    /**
     * Запрос на обновление данных пользователя
     *
     * @param person Данные пользователя
     */
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void updatePerson(@RequestBody Person person) {//todo после update обычно возвращают обновлённую сущность
        personService.personSave(person);
    }

    /**
     * Запрос данных всех пользователей подходящих под критерии поиска
     *
     * @param personId Идентификатор текущего пользователя
     * @return Список данных пользователей
     */
    @GetMapping("/{personId}/suitable")
    @ResponseStatus(HttpStatus.OK)
    public List<Person> findAllSuitablePersons(@PathVariable Long personId) {
        return personService.findAllSuitablePersons(personId);
    }

    /**
     * Запрос данных пользователя подходящего под критерии поиска
     * Список полходящих пользователей разивается на "страницы" (одна запись = одна страница)
     *
     * @param personId Идентификатор текущего пользователя
     * @param page     Порядковый номер пользователя
     * @return Данные пользователя
     */
    @GetMapping("/{personId}/suitable/{page}")
    @ResponseStatus(HttpStatus.OK)
    public Person findSuitablePerson(@PathVariable Long personId, @PathVariable int page) { //todo Pageable
        return personService.findSuitablePerson(personId, page);
    }

    /**
     * Запрос количества пользователей подходящих под критерии поиска
     *
     * @param personId Идентификатор текущего пользователя
     * @return Количество польщователей
     */
    @GetMapping("/{personId}/suitable/count")
    @ResponseStatus(HttpStatus.OK)
    public int getSuitablePersonsCount(@PathVariable Long personId) { //todo наименование get подходит только для getters
        return personService.getSuitablePersonsCount(personId);
    }

    /**
     * Запрос списка "любимых" пользователей
     *
     * @param personId Идентификатор пользователя
     * @return Список пользователей
     */
    @GetMapping("/{personId}/favorite")
    @ResponseStatus(HttpStatus.OK)
    public List<PersonDto> findAllFavoritePersons(@PathVariable Long personId) {
        return personService.findAllFavoritePersons(personId);
    }

    /**
     * Запрос данных "любимого" пользователя
     * Список "любимых" пользователей разивается на "страницы" (одна запись = одна страница)
     *
     * @param personId Идентификатор текущего пользователя
     * @param page     Порядковый номер пользователя
     * @return Данные пользователя
     */
    @GetMapping("/{personId}/favorite/{page}")
    @ResponseStatus(HttpStatus.OK)
    public PersonDto findFavoritePerson(@PathVariable Long personId, @PathVariable int page) {
        return personService.findFavoritePerson(personId, page);
    }

    /**
     * Запрос количества "любимых" пользователей
     *
     * @param personId Идентификатор текущего пользователя
     * @return Количество польщователей
     */
    @GetMapping("/{personId}/favorite/count")
    @ResponseStatus(HttpStatus.OK)
    public int getFavoritePersonsCount(@PathVariable Long personId) { //todo наименование get подходит только для getters
        return personService.getFavoritePersonsCount(personId);
    }

    /**
     * Запрос создания нового пользователя
     *
     * @param person Данные пользователя
     * @return Данные пользователя
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Person createPerson(@RequestBody Person person) {
        return personService.createPerson(person);
    }
}
