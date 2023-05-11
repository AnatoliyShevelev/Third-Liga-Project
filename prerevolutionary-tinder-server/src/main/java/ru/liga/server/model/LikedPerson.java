package ru.liga.server.model;

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
public class LikedPerson {
    // ниже вопрос todo вместо отдельной сущности стоит сделать связь ManyToMany
    //не понял что нужно сделать: нужно сделать связь многие ко многим внутри этого класса или связать его с классом Person по этому принципу?
    //todo проблема в том, что в БД у вас нету foreign key, связи одной таблицы с другой по personId
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "main_id")
    private Long mainId;

    @Column(name = "liked_id")
    private Long likedId;

    public LikedPerson(Long mainId, Long likedId) { //todo не используется..?
        this.mainId = mainId;
        this.likedId = likedId;
    }
}
