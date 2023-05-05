package ru.liga.server.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.liga.server.dto.PersonDto;
import ru.liga.server.model.Person;
import ru.liga.server.service.PersonService;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RestController
@RequestMapping("/persons")
@RequiredArgsConstructor
public class TranslateController { //DONE todo в наименовании можно не указывать Rest если других нет - TranslateController
    //DONE todo относится ко всем эндпоинтам - стоит возвращать ResponseEntity<>
    private final PersonService personService;

    /**
     * Запрос данных всех пользователей
     *
     * @return Список данных пользователей
     */
    @GetMapping
    public ResponseEntity<List<Person>> findAllPersons() {
        List<Person> persons = personService.findAll();
        return ResponseEntity.ok(persons);
    }

    /**
     * Запрос данных пользователя по идентификатору
     *
     * @param personId Идентификатор текущего пользователя
     * @return Данные пользователя
     */
    @GetMapping("/{personId}")
    public ResponseEntity<Person> findPerson(@PathVariable Long personId) {
        Person person = personService.findByPersonId(personId);
        return ResponseEntity.ok(person);
    }

    /**
     * Запрос на обновление данных пользователя
     *
     * @param person Данные пользователя
     */

    @PutMapping
    public ResponseEntity<Void> savePerson(@RequestBody Person person) {//DONE todo после update обычно возвращают обновлённую сущность
        personService.personSave(person);
        return ResponseEntity.ok().build();
    }

    /**
     * Запрос данных всех пользователей подходящих под критерии поиска
     *
     * @param personId Идентификатор текущего пользователя
     * @return Список данных пользователей
     */
    @GetMapping("/{personId}/suitable")
    public ResponseEntity<List<Person>> findAllSuitablePersons(@PathVariable Long personId, Pageable pageable) {
        List<Person> persons = personService.findAllSuitablePersons(personId, pageable);
        return ResponseEntity.ok(persons);
    }
    /**
     * Запрос данных пользователя подходящего под критерии поиска
     * Список полходящих пользователей разивается на "страницы" (одна запись = одна страница)
     *
     * @param personId Идентификатор текущего пользователя
     * @param pageable Порядковый номер пользователя
     * @return Данные пользователя
     */
    @GetMapping("/{personId}/suitable")
    public ResponseEntity<Person> findSuitablePerson(@PathVariable Long personId, Pageable pageable) {
        Person person = personService.findSuitablePerson(personId, pageable);
        if (person != null) {
            return new ResponseEntity<>(person, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Запрос количества пользователей подходящих под критерии поиска
     *
     * @param personId Идентификатор текущего пользователя
     * @return Количество польщователей
     */
    @GetMapping("/{personId}/suitable/count")
    public ResponseEntity<Integer> findSuitablePersonsCount(@PathVariable Long personId) {
        int count = personService.findSuitablePersonsCount(personId);
        return ResponseEntity.ok(count);
    }

    /**
     * Запрос списка "любимых" пользователей
     *
     * @param personId Идентификатор пользователя
     * @return Список пользователей
     */
    @GetMapping("/{personId}/favorite")
    public ResponseEntity<List<PersonDto>> findAllFavoritePersons(@PathVariable Long personId, Pageable pageable) {
        List<PersonDto> persons = personService.findAllFavoritePersons(personId, pageable);
        return ResponseEntity.ok(persons);
    }

    /**
     * Запрос данных "любимого" пользователя
     * Список "любимых" пользователей разивается на "страницы" (одна запись = одна страница)
     *
     * @param personId Идентификатор текущего пользователя
     * @param pageable Порядковый номер пользователя
     * @return Данные пользователя
     */
    @GetMapping("/{personId}/favorite")
    public ResponseEntity<PersonDto> findFavoritePerson(@PathVariable Long personId, Pageable pageable) {
        PersonDto person = personService.findFavoritePerson(personId, pageable);
        return ResponseEntity.ok(person);
    }

    /**
     * Запрос количества "любимых" пользователей
     *
     * @param personId Идентификатор текущего пользователя
     * @return Количество польщователей
     */

    @GetMapping("/{personId}/favorite/count")
    public ResponseEntity<Integer> findFavoritePersonsCount(@PathVariable Long personId) {//DONE todo наименование get подходит только для getters
        int count = personService.findFavoritePersonsCount(personId);
        return ResponseEntity.ok(count);
    }
    /**
     * Запрос создания нового пользователя
     *
     * @param person Данные пользователя
     * @return Данные пользователя
     */
    @PostMapping
    public ResponseEntity<Person> createPerson(@RequestBody Person person) {
        Person createdPerson = personService.createPerson(person);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPerson);
    }

}
