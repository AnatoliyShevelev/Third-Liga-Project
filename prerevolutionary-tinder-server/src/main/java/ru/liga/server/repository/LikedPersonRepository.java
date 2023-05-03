package ru.liga.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.liga.server.person_model.LikedPerson;

public interface LikedPersonRepository extends JpaRepository<LikedPerson, Long> {
    LikedPerson getByMainIdAndLikedId(Long mainId, Long likedId); //todo стоит обернуть в Optional<> и это метод find
}
