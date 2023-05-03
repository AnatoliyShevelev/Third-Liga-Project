package ru.liga.tgbot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import ru.liga.tgbot.config.RestTemplateConfig;
import ru.liga.tgbot.dto.LikedPersonDTO;
import ru.liga.tgbot.dto.PersonDTO;
import ru.liga.tgbot.enums.BotState;
import ru.liga.tgbot.model.Person;
import ru.liga.tgbot.enums.Sex;

import java.net.URI;
import java.net.URISyntaxException;

@Service
public class PersonService { //todo разве это не адаптер для взаимодействия с сервером?
    @Value("${person.url}")
    private String personsUrl; //todo лучше перенести в конфиг
    @Value("${favorite.url}")
    private String favoriteUrl; //todo лучше перенести в конфиг
    @Autowired
    private RestTemplateConfig restTemplateConfig;

    /**
     * Сервис для создания профиля пользователя
     *
     * @param person создаваемый Person
     * @return созданная запись
     * @throws URISyntaxException
     */
    public PersonDTO createPerson(Person person) throws URISyntaxException {
        HttpHeaders headers = getHttpHeaders();
        URI url = new URI(personsUrl);
        PersonDTO objEmp = new PersonDTO(person);

        HttpEntity<PersonDTO> requestEntity = new HttpEntity<>(objEmp, headers);
        return restTemplateConfig.getRestTemplate().postForObject(url, requestEntity, PersonDTO.class);
    }

    /**
     * Сервис получения профиля пользователя
     *
     * @param userId Id текущего пользователя из Телеграмма
     * @return Полученный профиль
     * @throws URISyntaxException
     */
    public PersonDTO getPerson(Long userId) throws URISyntaxException { //todo не get
        URI url = new URI(personsUrl + userId);

        return restTemplateConfig.getRestTemplate().getForObject(url, PersonDTO.class);
    }

    /**
     * Сервис получения профиля, который можно лайкнуть
     *
     * @param userId Id текущего пользователя из Телеграмма
     * @param page   номер страницы
     * @return Полученный профиль
     * @throws URISyntaxException
     */
    public PersonDTO getSuitablePerson(Long userId, int page) throws URISyntaxException { //todo не get
        URI url = new URI(personsUrl + userId + "/suitable/" + page);
        return restTemplateConfig.getRestTemplate().getForObject(url, PersonDTO.class);
    }

    /**
     * Сервис получения любимца
     *
     * @param userId Id текущего пользователя из Телеграмма
     * @param page   номер страницы
     * @return Полученный профиль любимца
     * @throws URISyntaxException
     */
    public PersonDTO getFavoritePerson(Long userId, int page) throws URISyntaxException { //todo не get
        URI url = new URI(personsUrl + userId + "/favorite/" + page);
        return restTemplateConfig.getRestTemplate().getForObject(url, PersonDTO.class);
    }

    /**
     * Сервис получения количества профилей, которые можно лайкнуть
     *
     * @param userId Id текущего пользователя из Телеграмма
     * @return Кол-во анкет
     * @throws URISyntaxException
     */
    public Integer getCountSuitablePerson(Long userId) throws URISyntaxException { //todo не get
        URI url = new URI(personsUrl + userId + "/suitable/count");
        return restTemplateConfig.getRestTemplate().getForObject(url, Integer.class);
    }

    /**
     * Сервис получения количества любимцев
     *
     * @param userId Id текущего пользователя из Телеграмма
     * @return Кол-во анкет
     * @throws URISyntaxException
     */
    public Integer getCountFavoritePerson(Long userId) throws URISyntaxException { //todo не get
        URI url = new URI(personsUrl + userId + "/favorite/count");
        return restTemplateConfig.getRestTemplate().getForObject(url, Integer.class);
    }

    /**
     * Сервис проставления лайка
     *
     * @param userId Id текущего пользователя из Телеграмма
     * @param likedPersonId Id профиля, которому ставим лайка
     * @throws URISyntaxException
     */
    public void likePerson(Long userId, Long likedPersonId) throws URISyntaxException {
        HttpHeaders headers = getHttpHeaders();
        URI url = new URI(favoriteUrl);
        HttpEntity<LikedPersonDTO> requestEntity = new HttpEntity<>(new LikedPersonDTO(userId, likedPersonId), headers);
        restTemplateConfig.getRestTemplate().postForObject(url, requestEntity, LikedPersonDTO.class);
    }

    /**
     * Получение хедера
     *
     * @return готовые хедеры
     */
    private HttpHeaders getHttpHeaders() { //todo не get.. Дублирование метода, значит стоит вынести
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
