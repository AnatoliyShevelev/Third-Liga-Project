package ru.liga.server.model;//DONE todo в названиях пакетов обычно не используют _

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
@Table(name = "liked_person")
public class LikedPerson { //todo вместо отдельной сущности стоит сделать связь ManyToMany
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "main_id")
    private Long mainId;

    @Column(name = "liked_id")
    private Long likedId;

    public LikedPerson(Long mainId, Long likedId) {
        this.mainId = mainId;
        this.likedId = likedId;
    }
}
