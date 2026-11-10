package az.iktex.organization.structure.testders.service;

import az.iktex.organization.structure.testders.entity.Course;
import az.iktex.organization.structure.testders.entity.Student;
import az.iktex.organization.structure.testders.mapper.CourseMapper;
import az.iktex.organization.structure.testders.mapper.StudentMapper;
import az.iktex.organization.structure.testders.repository.CourseRepository;
import az.iktex.organization.structure.testders.repository.StudentRepository;
import az.iktex.organization.structure.testders.request.CourseRequest;
import az.iktex.organization.structure.testders.response.CourseResponse;
import az.iktex.organization.structure.testders.response.StudentResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourseService {

    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final CourseMapper courseMapper;
    private final StudentMapper studentMapper;

    @Transactional
    public CourseResponse create(CourseRequest request) {
        if (courseRepository.existsByTitle(request.title())) {
            throw new IllegalStateException("Course title already exists: " + request.title());
        }
        Course course = courseMapper.toEntity(request);
        return courseMapper.toResponse(courseRepository.save(course));
    }

    public List<CourseResponse> getAll() {
        return courseMapper.toResponseList(courseRepository.findAll());
    }

    public CourseResponse getById(Long id) {
        return courseMapper.toResponse(findCourse(id));
    }

    public List<CourseResponse> getByStudent(Long studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new EntityNotFoundException("Student not found: " + studentId);
        }
        return courseMapper.toResponseList(courseRepository.findAllByStudentsId(studentId));
    }

    public List<StudentResponse> getStudents(Long courseId) {
        if (!courseRepository.existsById(courseId)) {
            throw new EntityNotFoundException("Course not found: " + courseId);
        }
        return studentMapper.toResponseList(studentRepository.findAllByCoursesId(courseId));
    }

    public long countStudents(Long courseId) {
        if (!courseRepository.existsById(courseId)) {
            throw new EntityNotFoundException("Course not found: " + courseId);
        }
        return courseRepository.countStudentsByCourseId(courseId);
    }

    @Transactional
    public CourseResponse update(Long id, CourseRequest request) {
        Course course = findCourse(id);
        if (courseRepository.existsByTitleAndIdNot(request.title(), id)) {
            throw new IllegalStateException("Course title already exists: " + request.title());
        }
        courseMapper.updateEntity(request, course);
        return courseMapper.toResponse(course);
    }

    @Transactional
    public void delete(Long id) {
        Course course = courseRepository.findByIdWithStudents(id)
                .orElseThrow(() -> new EntityNotFoundException("Course not found: " + id));
        // Course inverse tərəfdir (mappedBy = "courses"): student_course cədvəlini yalnız Student idarə edir.
        // Kursu birbaşa silsək, Hibernate student_course sətirlərinə toxunmaz və
        // fk_sc_course xətası çıxar. Ona görə əvvəlcə hər tələbənin courses-undan bu kursu çıxarırıq,
        // Hibernate həmin sətirləri silir, sonra kursun özü silinir.
        for (Student student : new ArrayList<>(course.getStudents())) {
            student.removeCourse(course);
        }
        courseRepository.delete(course);
    }

    private Course findCourse(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course not found: " + id));
    }
}
