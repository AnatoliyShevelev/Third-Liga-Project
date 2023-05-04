package ru.liga.server.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.liga.server.model.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {
    Person findByPersonId(Long personId);

    @Query("select distinct p1 " +
            "from Person p1, Person p2 " +
            "where p1.genderSearch in (p2.gender, 'ALL') " +
            "and (p1.gender = p2.genderSearch or (p2.genderSearch = 'ALL' and p1.gender in ('MALE', 'FEMALE'))) " +
            "and (p1.personId <> ?1 or p1.personId is null) " +
            "and p2.personId = ?1")
    Page<Person> findSuitablePersons(Long personId, Pageable pageable);

    @Query("select distinct count(p1.id) " +
            "from Person p1, Person p2 " +
            "where p1.genderSearch in (p2.gender, 'ALL') " +
            "and (p1.gender = p2.genderSearch or (p2.genderSearch = 'ALL' and p1.gender in ('MALE', 'FEMALE'))) " +
            "and (p1.personId <> ?1 or p1.personId is null) " +
            "and p2.personId = ?1")
    int getSuitablePersonsCount(Long personId);

    @Query("select distinct p " +
            "from Person p, Person pm, LikedPerson lp1, LikedPerson lp2 " +
            "where  " +
            "(lp1.likedId = p.id and pm.id = lp1.mainId or " +
            " lp2.mainId = p.id and pm.id = lp2.likedId) and " +
            "pm.personId = ?1")
    Page<Person> findLikedPersons(Long personId, Pageable pageable);

    @Query("select count(distinct p.id) " +
            "from Person p, Person pm, LikedPerson lp1, LikedPerson lp2 " +
            "where  " +
            "(lp1.likedId = p.id and pm.id = lp1.mainId or " +
            " lp2.mainId = p.id and pm.id = lp2.likedId) and " +
            "pm.personId = ?1")
    int getLikedPersonsCount(Long personId);
}
