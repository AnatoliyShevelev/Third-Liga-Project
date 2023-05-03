package ru.liga.server.person_model;//todo в названиях пакетов обычно не используют _

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Модель данных связи пользователей
 */
@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "LIKED_PERSON")
public class LikedPerson { //todo вместо отдельной сущности стоит сделать связь ManyToMany
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "MAIN_ID")
    private Long mainId;

    @Column(name = "LIKED_ID")
    private Long likedId;

    public LikedPerson(Long mainId, Long likedId) {
        this.mainId = mainId;
        this.likedId = likedId;
    }
}
