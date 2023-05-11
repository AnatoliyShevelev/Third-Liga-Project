package ru.liga.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.liga.server.model.LikedPerson;
import java.util.Optional;

public interface LikedPersonRepository extends JpaRepository<LikedPerson, Long> {
    Optional<LikedPerson> findByMainIdAndLikedId(Long mainId, Long likedId);
}