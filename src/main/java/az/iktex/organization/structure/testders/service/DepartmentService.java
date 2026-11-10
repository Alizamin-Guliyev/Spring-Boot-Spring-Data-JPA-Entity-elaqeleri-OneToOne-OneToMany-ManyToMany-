package az.iktex.organization.structure.testders.service;

import az.iktex.organization.structure.testders.entity.Department;
import az.iktex.organization.structure.testders.mapper.DepartmentMapper;
import az.iktex.organization.structure.testders.repository.DepartmentRepository;
import az.iktex.organization.structure.testders.request.DepartmentRequest;
import az.iktex.organization.structure.testders.response.DepartmentResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    @Transactional
    public DepartmentResponse create(DepartmentRequest request) {
        if (departmentRepository.existsByName(request.name())) {
            throw new IllegalStateException("Department name already exists: " + request.name());
        }
        Department department = departmentMapper.toEntity(request);
        return departmentMapper.toResponse(departmentRepository.save(department));
    }

    public DepartmentResponse getById(Long id) {
        return departmentMapper.toResponse(findDepartmentWithEmployees(id));
    }

    public List<DepartmentResponse> getAll() {
        return departmentMapper.toResponseList(departmentRepository.findAll());
    }

    public List<DepartmentResponse> getWithoutEmployees() {
        return departmentMapper.toResponseList(departmentRepository.findByEmployeesIsEmpty());
    }

    @Transactional
    public DepartmentResponse update(Long id, DepartmentRequest request) {
        Department department = findDepartmentWithEmployees(id);
        if (departmentRepository.existsByNameAndIdNot(request.name(), id)) {
            throw new IllegalStateException("Department name already exists: " + request.name());
        }
        departmentMapper.updateEntity(request, department);
        return departmentMapper.toResponse(department);
    }

    @Transactional
    public void delete(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Department not found: " + id));
        if (departmentRepository.hasEmployees(id)) {
            throw new IllegalStateException("Department has employees and cannot be deleted: " + id);
        }
        departmentRepository.delete(department);
    }

    private Department findDepartmentWithEmployees(Long id) {
        return departmentRepository.findByIdWithEmployees(id)
                .orElseThrow(() -> new EntityNotFoundException("Department not found: " + id));
    }
}
