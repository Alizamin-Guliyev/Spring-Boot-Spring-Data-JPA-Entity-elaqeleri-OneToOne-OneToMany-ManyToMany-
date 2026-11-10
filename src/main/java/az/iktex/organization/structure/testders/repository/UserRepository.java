package az.iktex.organization.structure.testders.repository;

import az.iktex.organization.structure.testders.entity.User;
import az.iktex.organization.structure.testders.enums.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // ORM (derived query)

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);

    List<User> findByPassportIsNull();

    // JPQL

    @Query("select u from User u left join fetch u.passport where u.id = :id")
    Optional<User> findByIdWithPassport(@Param("id") Long id);

    @Query("select u from User u left join fetch u.passport where u.email = :email")
    Optional<User> findByEmailWithPassport(@Param("email") String email);

    @Query("select u from User u left join fetch u.passport")
    List<User> findAllWithPassport();

    @Query("select u from User u left join fetch u.passport where u.gender = :gender")
    List<User> findAllByGenderWithPassport(@Param("gender") Gender gender);
}
