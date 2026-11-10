package az.iktex.organization.structure.testders.mapper;

import az.iktex.organization.structure.testders.entity.Student;
import az.iktex.organization.structure.testders.request.StudentRequest;
import az.iktex.organization.structure.testders.response.StudentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = CourseMapper.class)
public interface StudentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "courses", ignore = true)
    Student toEntity(StudentRequest request);

    @Mapping(target = "courses", ignore = true)
    StudentResponse toResponse(Student student);

    @Named("withCourses")
    StudentResponse toDetailResponse(Student student);

    List<StudentResponse> toResponseList(List<Student> students);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "courses", ignore = true)
    void updateEntity(StudentRequest request, @MappingTarget Student student);
}
