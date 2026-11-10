package az.iktex.organization.structure.testders.controller;

import az.iktex.organization.structure.testders.request.EmployeeRequest;
import az.iktex.organization.structure.testders.response.EmployeeResponse;
import az.iktex.organization.structure.testders.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeResponse create(@RequestBody EmployeeRequest request) {
        return employeeService.create(request);
    }

    @GetMapping("/{id}")
    public EmployeeResponse getById(@PathVariable Long id) {
        return employeeService.getById(id);
    }

    @GetMapping("/department/{departmentId}")
    public List<EmployeeResponse> getByDepartment(@PathVariable Long departmentId) {
        return employeeService.getByDepartment(departmentId);
    }

    @GetMapping("/salary-range")
    public List<EmployeeResponse> getBySalaryRange(@RequestParam BigDecimal min, @RequestParam BigDecimal max) {
        return employeeService.getBySalaryRange(min, max);
    }

    @GetMapping("/department/{id}/average-salary")
    public BigDecimal getAverageSalary(@PathVariable Long id) {
        return employeeService.getAverageSalary(id);
    }

    @PutMapping("/{id}")
    public EmployeeResponse update(@PathVariable Long id, @RequestBody EmployeeRequest request) {
        return employeeService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        employeeService.delete(id);
    }
}
