package ru.liga.server.person_model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Модель данных пользователя
 */
@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "PERSON") //todo почему тут капсом?
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "GENDER")
    private String gender;

    @Column(name = "PERSON_ID")
    private Long personId;

    @Column(name = "FULL_NAME")
    private String fullName;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "GENDER_SEARCH")
    private String genderSearch;
}
