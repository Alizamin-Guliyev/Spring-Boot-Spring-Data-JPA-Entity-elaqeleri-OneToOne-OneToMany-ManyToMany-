package az.iktex.organization.structure.testders.repository;

import az.iktex.organization.structure.testders.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    // ORM (derived query)

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);

    List<Department> findByEmployeesIsEmpty();

    // JPQL

    @Query("select d from Department d left join fetch d.employees where d.id = :id")
    Optional<Department> findByIdWithEmployees(@Param("id") Long id);

    @Query("select count(e) > 0 from Employee e where e.department.id = :departmentId")
    boolean hasEmployees(@Param("departmentId") Long departmentId);
}
