package az.iktex.organization.structure.testders.service;

import az.iktex.organization.structure.testders.entity.Department;
import az.iktex.organization.structure.testders.entity.Employee;
import az.iktex.organization.structure.testders.mapper.EmployeeMapper;
import az.iktex.organization.structure.testders.repository.DepartmentRepository;
import az.iktex.organization.structure.testders.repository.EmployeeRepository;
import az.iktex.organization.structure.testders.request.EmployeeRequest;
import az.iktex.organization.structure.testders.response.EmployeeResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeMapper employeeMapper;

    @Transactional
    public EmployeeResponse create(EmployeeRequest request) {
        Department department = findDepartment(request.departmentId());
        if (employeeRepository.existsByNameAndSurnameAndDepartmentId(
                request.name(), request.surname(), request.departmentId())) {
            throw new IllegalStateException("Employee already exists in this department: "
                    + request.name() + " " + request.surname());
        }
        Employee employee = employeeMapper.toEntity(request);
        department.addEmployee(employee);
        return employeeMapper.toResponse(employeeRepository.save(employee));
    }

    public EmployeeResponse getById(Long id) {
        return employeeMapper.toResponse(findEmployeeWithDepartment(id));
    }

    public List<EmployeeResponse> getByDepartment(Long departmentId) {
        if (!departmentRepository.existsById(departmentId)) {
            throw new EntityNotFoundException("Department not found: " + departmentId);
        }
        return employeeMapper.toResponseList(employeeRepository.findAllByDepartmentId(departmentId));
    }

    public List<EmployeeResponse> getBySalaryRange(BigDecimal min, BigDecimal max) {
        if (min.compareTo(max) > 0) {
            throw new IllegalArgumentException("min must not be greater than max");
        }
        return employeeMapper.toResponseList(employeeRepository.findAllBySalaryBetween(min, max));
    }

    public BigDecimal getAverageSalary(Long departmentId) {
        if (!departmentRepository.existsById(departmentId)) {
            throw new EntityNotFoundException("Department not found: " + departmentId);
        }
        Double average = employeeRepository.findAverageSalaryByDepartmentId(departmentId);
        return average == null
                ? BigDecimal.ZERO
                : BigDecimal.valueOf(average).setScale(2, RoundingMode.HALF_UP);
    }

    @Transactional
    public EmployeeResponse update(Long id, EmployeeRequest request) {
        Employee employee = findEmployeeWithDepartment(id);
        if (employeeRepository.existsByNameAndSurnameAndDepartmentIdAndIdNot(
                request.name(), request.surname(), request.departmentId(), id)) {
            throw new IllegalStateException("Employee already exists in this department: "
                    + request.name() + " " + request.surname());
        }
        employeeMapper.updateEntity(request, employee);
        if (!employee.getDepartment().getId().equals(request.departmentId())) {
            employee.setDepartment(findDepartment(request.departmentId()));
        }
        return employeeMapper.toResponse(employee);
    }

    @Transactional
    public void delete(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found: " + id));
        employeeRepository.delete(employee);
    }

    private Employee findEmployeeWithDepartment(Long id) {
        return employeeRepository.findByIdWithDepartment(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found: " + id));
    }

    private Department findDepartment(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Department not found: " + id));
    }
}
