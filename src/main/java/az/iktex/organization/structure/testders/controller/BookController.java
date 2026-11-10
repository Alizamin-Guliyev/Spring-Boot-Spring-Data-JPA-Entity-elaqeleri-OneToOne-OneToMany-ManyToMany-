package az.iktex.organization.structure.testders.controller;

import az.iktex.organization.structure.testders.request.BookRequest;
import az.iktex.organization.structure.testders.response.BookResponse;
import az.iktex.organization.structure.testders.service.BookService;
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
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookResponse create(@RequestBody BookRequest request) {
        return bookService.create(request);
    }

    @GetMapping("/{id}")
    public BookResponse getById(@PathVariable Long id) {
        return bookService.getById(id);
    }

    @GetMapping
    public List<BookResponse> getAll(@RequestParam(required = false) String title) {
        return bookService.getAll(title);
    }

    @GetMapping("/cheaper-than")
    public List<BookResponse> getCheaperThan(@RequestParam BigDecimal price) {
        return bookService.getCheaperThan(price);
    }

    @PutMapping("/{id}")
    public BookResponse update(@PathVariable Long id, @RequestBody BookRequest request) {
        return bookService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        bookService.delete(id);
    }

    @PostMapping("/{bookId}/authors/{authorId}")
    @ResponseStatus(HttpStatus.CREATED)
    public BookResponse addAuthor(@PathVariable Long bookId, @PathVariable Long authorId) {
        return bookService.addAuthor(bookId, authorId);
    }

    @DeleteMapping("/{bookId}/authors/{authorId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeAuthor(@PathVariable Long bookId, @PathVariable Long authorId) {
        bookService.removeAuthor(bookId, authorId);
    }
}
