package az.iktex.organization.structure.testders.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "students")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "surname", nullable = false, length = 50)
    private String surname;

    @Column(name = "age", nullable = false)
    private Integer age;

    // Niyə CascadeType.REMOVE yoxdur?
    // Kurs tələbəyə məxsus deyil, onu başqa tələbələr də keçir. REMOVE olsaydı, tələbə silinəndə
    // onun bütün kursları da silinərdi (və ya digər tələbələrin əlaqəsinə görə FK xətası çıxardı).
    // REMOVE olmadan tələbə silinəndə Hibernate yalnız student_course cədvəlindəki
    // həmin tələbənin sətirlərini silir, kurslar isə qalır.
    // Student owner tərəfdir (@JoinTable burada): student_course sətirlərini yalnız bu kolleksiya idarə edir.
    // PERSIST, MERGE: yeni kurs tələbə ilə birlikdə saxlana bilər, amma silinmə ötürülmür.
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(name = "student_course",
            joinColumns = @JoinColumn(name = "student_id",
                    foreignKey = @ForeignKey(name = "fk_sc_student")),
            inverseJoinColumns = @JoinColumn(name = "course_id",
                    foreignKey = @ForeignKey(name = "fk_sc_course")),
            uniqueConstraints = @UniqueConstraint(name = "uk_student_course",
                    columnNames = {"student_id", "course_id"}))
    private Set<Course> courses = new HashSet<>();

    public void addCourse(Course course) {
        courses.add(course);
        course.getStudents().add(this);
    }

    public void removeCourse(Course course) {
        courses.remove(course);
        course.getStudents().remove(this);
    }
}
