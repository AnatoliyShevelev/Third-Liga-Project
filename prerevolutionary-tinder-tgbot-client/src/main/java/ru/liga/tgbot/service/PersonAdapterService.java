package ru.liga.tgbot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import ru.liga.tgbot.config.HttpHeadersConfig;
import ru.liga.tgbot.config.PersonServiceConfig;
import ru.liga.tgbot.config.RestTemplateConfig;
import ru.liga.tgbot.dto.LikedPersonDto;
import ru.liga.tgbot.dto.PersonDto;
import ru.liga.tgbot.mapper.PersonMapper;
import ru.liga.tgbot.model.Person;

import java.net.URI;
import java.net.URISyntaxException;

@Service
public class PersonAdapterService { //todo где тесты..?
    @Autowired  //todo лучше использовать @RequiredArgsConstructor и сделать поле final, это относится и к след полям
    private PersonServiceConfig personServiceConfig;
    @Autowired
    private RestTemplateConfig restTemplateConfig;

    /**
     * Сервис для создания профиля пользователя
     *
     * @param person создаваемый Person
     * @return созданная запись
     * @throws URISyntaxException
     */
    public PersonDto createPerson(Person person) throws URISyntaxException { //todo не используется..?
        HttpHeaders headers = new HttpHeadersConfig().settingHttpHeaders();
        URI url = new URI(personServiceConfig.getPersonsUrl());
        PersonDto objEmp = PersonMapper.fillPersonDto(person);

        HttpEntity<PersonDto> requestEntity = new HttpEntity<>(objEmp, headers);
        return restTemplateConfig.restTemplate().postForObject(url, requestEntity, PersonDto.class);
    }

    /**
     * Сервис получения профиля пользователя
     *
     * @param userId Id текущего пользователя из Телеграмма
     * @return Полученный профиль
     * @throws URISyntaxException
     */
    public PersonDto findPerson(Long userId) throws URISyntaxException {
        URI url = new URI(personServiceConfig.getPersonsUrl() + userId);
        return restTemplateConfig.restTemplate().getForObject(url, PersonDto.class);
    }

    /**
     * Сервис получения профиля, который можно лайкнуть
     *
     * @param userId Id текущего пользователя из Телеграмма
     * @param page   номер страницы
     * @return Полученный профиль
     * @throws URISyntaxException
     */
    public PersonDto findSuitablePerson(Long userId, int page) throws URISyntaxException {
        URI url = new URI(personServiceConfig.getPersonsUrl() + userId + "/suitable/" + page);
        return restTemplateConfig.restTemplate().getForObject(url, PersonDto.class);
    }

    /**
     * Сервис получения любимца
     *
     * @param userId Id текущего пользователя из Телеграмма
     * @param page   номер страницы
     * @return Полученный профиль любимца
     * @throws URISyntaxException
     */
    public PersonDto findFavoritePerson(Long userId, int page) throws URISyntaxException {
        URI url = new URI(personServiceConfig.getPersonsUrl() + userId + "/favorite/" + page);
        return restTemplateConfig.restTemplate().getForObject(url, PersonDto.class);
    }

    /**
     * Сервис получения количества профилей, которые можно лайкнуть
     *
     * @param userId Id текущего пользователя из Телеграмма
     * @return Кол-во анкет
     * @throws URISyntaxException
     */
    public Integer findCountSuitablePerson(Long userId) throws URISyntaxException {
        URI url = new URI(personServiceConfig.getPersonsUrl() + userId + "/suitable/count");
        return restTemplateConfig.restTemplate().getForObject(url, Integer.class);
    }

    /**
     * Сервис получения количества любимцев
     *
     * @param userId Id текущего пользователя из Телеграмма
     * @return Кол-во анкет
     * @throws URISyntaxException
     */
    public Integer findCountFavoritePerson(Long userId) throws URISyntaxException {
        URI url = new URI(personServiceConfig.getPersonsUrl() + userId + "/favorite/count");
        return restTemplateConfig.restTemplate().getForObject(url, Integer.class);
    }

    /**
     * Сервис проставления лайка
     *
     * @param userId Id текущего пользователя из Телеграмма
     * @param likedPersonId Id профиля, которому ставим лайка
     * @throws URISyntaxException
     */
    public void likePerson(Long userId, Long likedPersonId) throws URISyntaxException {
        HttpHeaders headers = new HttpHeadersConfig().settingHttpHeaders();
        URI url = new URI(personServiceConfig.getFavoriteUrl());
        HttpEntity<LikedPersonDto> requestEntity = new HttpEntity<>(new LikedPersonDto(userId, likedPersonId), headers);
        restTemplateConfig.restTemplate().postForObject(url, requestEntity, LikedPersonDto.class);
    }


}
