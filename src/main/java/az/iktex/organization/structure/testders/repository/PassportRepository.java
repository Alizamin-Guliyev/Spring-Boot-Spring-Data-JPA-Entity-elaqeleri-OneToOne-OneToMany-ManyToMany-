package az.iktex.organization.structure.testders.repository;

import az.iktex.organization.structure.testders.entity.Passport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PassportRepository extends JpaRepository<Passport, Long> {

    // ORM (derived query)

    boolean existsBySerialNumber(String serialNumber);

    boolean existsByUserId(Long userId);

    Optional<Passport> findByUserId(Long userId);

    // JPQL

    @Query("select p from Passport p join fetch p.user where p.serialNumber = :serialNumber")
    Optional<Passport> findBySerialNumberWithUser(@Param("serialNumber") String serialNumber);

    @Query("select p from Passport p where p.issueDate < :date order by p.issueDate")
    List<Passport> findAllIssuedBefore(@Param("date") LocalDate date);
}
