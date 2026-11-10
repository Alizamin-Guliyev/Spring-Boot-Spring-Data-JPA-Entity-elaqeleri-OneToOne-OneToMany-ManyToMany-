package az.iktex.organization.structure.testders.controller;

import az.iktex.organization.structure.testders.enums.Gender;
import az.iktex.organization.structure.testders.request.PassportRequest;
import az.iktex.organization.structure.testders.request.UserRequest;
import az.iktex.organization.structure.testders.response.PassportResponse;
import az.iktex.organization.structure.testders.response.UserResponse;
import az.iktex.organization.structure.testders.service.UserService;
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

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse create(@RequestBody UserRequest request) {
        return userService.create(request);
    }

    @GetMapping("/{id}")
    public UserResponse getById(@PathVariable Long id) {
        return userService.getById(id);
    }

    @GetMapping("/by-email")
    public UserResponse getByEmail(@RequestParam String email) {
        return userService.getByEmail(email);
    }

    @GetMapping
    public List<UserResponse> getAll(@RequestParam(required = false) Gender gender) {
        return userService.getAll(gender);
    }

    @GetMapping("/without-passport")
    public List<UserResponse> getWithoutPassport() {
        return userService.getWithoutPassport();
    }

    @PutMapping("/{id}")
    public UserResponse update(@PathVariable Long id, @RequestBody UserRequest request) {
        return userService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }

    @PostMapping("/{id}/passport")
    @ResponseStatus(HttpStatus.CREATED)
    public PassportResponse addPassport(@PathVariable Long id, @RequestBody PassportRequest request) {
        return userService.addPassport(id, request);
    }

    @GetMapping("/{id}/passport")
    public PassportResponse getPassport(@PathVariable Long id) {
        return userService.getPassport(id);
    }

    @DeleteMapping("/{id}/passport")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePassport(@PathVariable Long id) {
        userService.deletePassport(id);
    }
}
