package az.iktex.organization.structure.testders.repository;

import az.iktex.organization.structure.testders.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {

    // ORM (derived query)

    boolean existsByTitle(String title);

    boolean existsByTitleAndIdNot(String title, Long id);

    List<Course> findAllByStudentsId(Long studentId);

    // JPQL

    @Query("select c from Course c left join fetch c.students where c.id = :id")
    Optional<Course> findByIdWithStudents(@Param("id") Long id);

    @Query("select size(c.students) from Course c where c.id = :courseId")
    int countStudentsByCourseId(@Param("courseId") Long courseId);
}
