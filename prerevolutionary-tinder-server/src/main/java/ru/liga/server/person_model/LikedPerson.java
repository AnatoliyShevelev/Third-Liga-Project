package ru.liga.server.person_model;

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
public class LikedPerson {
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
