package az.iktex.organization.structure.testders.service;

import az.iktex.organization.structure.testders.entity.Course;
import az.iktex.organization.structure.testders.entity.Student;
import az.iktex.organization.structure.testders.mapper.StudentMapper;
import az.iktex.organization.structure.testders.repository.CourseRepository;
import az.iktex.organization.structure.testders.repository.StudentRepository;
import az.iktex.organization.structure.testders.request.StudentRequest;
import az.iktex.organization.structure.testders.response.StudentResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudentService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final StudentMapper studentMapper;

    @Transactional
    public StudentResponse create(StudentRequest request) {
        Student student = studentMapper.toEntity(request);
        return studentMapper.toResponse(studentRepository.save(student));
    }

    public StudentResponse getById(Long id) {
        return studentMapper.toDetailResponse(findStudentWithCourses(id));
    }

    public List<StudentResponse> getAll() {
        return studentMapper.toResponseList(studentRepository.findAll());
    }

    public List<StudentResponse> getWithoutCourses() {
        return studentMapper.toResponseList(studentRepository.findAllWithoutCourses());
    }

    @Transactional
    public StudentResponse update(Long id, StudentRequest request) {
        Student student = findStudent(id);
        studentMapper.updateEntity(request, student);
        return studentMapper.toResponse(student);
    }

    @Transactional
    public void delete(Long id) {
        studentRepository.delete(findStudent(id));
    }

    @Transactional
    public StudentResponse enroll(Long studentId, Long courseId) {
        Student student = findStudentWithCourses(studentId);
        Course course = findCourse(courseId);
        if (studentRepository.existsByIdAndCoursesId(studentId, courseId)) {
            throw new IllegalStateException("Student " + studentId + " is already enrolled in course " + courseId);
        }
        student.addCourse(course);
        return studentMapper.toDetailResponse(student);
    }

    @Transactional
    public void unenroll(Long studentId, Long courseId) {
        Student student = findStudentWithCourses(studentId);
        Course course = findCourse(courseId);
        if (!studentRepository.existsByIdAndCoursesId(studentId, courseId)) {
            throw new IllegalStateException("Student " + studentId + " is not enrolled in course " + courseId);
        }
        student.removeCourse(course);
    }

    private Student findStudent(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Student not found: " + id));
    }

    private Student findStudentWithCourses(Long id) {
        return studentRepository.findByIdWithCourses(id)
                .orElseThrow(() -> new EntityNotFoundException("Student not found: " + id));
    }

    private Course findCourse(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course not found: " + id));
    }
}
