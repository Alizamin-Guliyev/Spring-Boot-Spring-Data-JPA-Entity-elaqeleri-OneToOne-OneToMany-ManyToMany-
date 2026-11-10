package az.iktex.organization.structure.testders.controller;

import az.iktex.organization.structure.testders.request.AuthorRequest;
import az.iktex.organization.structure.testders.response.AuthorResponse;
import az.iktex.organization.structure.testders.response.BookResponse;
import az.iktex.organization.structure.testders.service.AuthorService;
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
@RequestMapping("/api/authors")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AuthorResponse create(@RequestBody AuthorRequest request) {
        return authorService.create(request);
    }

    @GetMapping("/{id}")
    public AuthorResponse getById(@PathVariable Long id) {
        return authorService.getById(id);
    }

    @GetMapping
    public List<AuthorResponse> getAll(@RequestParam(required = false) String country) {
        return authorService.getAll(country);
    }

    @GetMapping("/without-books")
    public List<AuthorResponse> getWithoutBooks() {
        return authorService.getWithoutBooks();
    }

    @GetMapping("/{id}/books")
    public List<BookResponse> getBooks(@PathVariable Long id) {
        return authorService.getBooks(id);
    }

    @PutMapping("/{id}")
    public AuthorResponse update(@PathVariable Long id, @RequestBody AuthorRequest request) {
        return authorService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        authorService.delete(id);
    }
}
