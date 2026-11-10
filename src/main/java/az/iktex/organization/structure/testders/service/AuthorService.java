package az.iktex.organization.structure.testders.service;

import az.iktex.organization.structure.testders.entity.Author;
import az.iktex.organization.structure.testders.mapper.AuthorMapper;
import az.iktex.organization.structure.testders.mapper.BookMapper;
import az.iktex.organization.structure.testders.repository.AuthorRepository;
import az.iktex.organization.structure.testders.repository.BookRepository;
import az.iktex.organization.structure.testders.request.AuthorRequest;
import az.iktex.organization.structure.testders.response.AuthorResponse;
import az.iktex.organization.structure.testders.response.BookResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final AuthorMapper authorMapper;
    private final BookMapper bookMapper;

    @Transactional
    public AuthorResponse create(AuthorRequest request) {
        if (authorRepository.existsByNameAndSurname(request.name(), request.surname())) {
            throw new IllegalStateException("Author already exists: " + request.name() + " " + request.surname());
        }
        Author author = authorMapper.toEntity(request);
        return authorMapper.toResponse(authorRepository.save(author));
    }

    public AuthorResponse getById(Long id) {
        return authorMapper.toResponse(findAuthor(id));
    }

    public List<AuthorResponse> getAll(String country) {
        List<Author> authors = country == null
                ? authorRepository.findAll()
                : authorRepository.findAllByCountry(country);
        return authorMapper.toResponseList(authors);
    }

    public List<AuthorResponse> getWithoutBooks() {
        return authorMapper.toResponseList(authorRepository.findAllWithoutBooks());
    }

    public List<BookResponse> getBooks(Long authorId) {
        if (!authorRepository.existsById(authorId)) {
            throw new EntityNotFoundException("Author not found: " + authorId);
        }
        return bookMapper.toResponseList(bookRepository.findAllByAuthorId(authorId));
    }

    @Transactional
    public AuthorResponse update(Long id, AuthorRequest request) {
        Author author = findAuthor(id);
        if (authorRepository.existsByNameAndSurnameAndIdNot(request.name(), request.surname(), id)) {
            throw new IllegalStateException("Author already exists: " + request.name() + " " + request.surname());
        }
        authorMapper.updateEntity(request, author);
        return authorMapper.toResponse(author);
    }

    @Transactional
    public void delete(Long id) {
        // cascade = ALL + orphanRemoval: yalnız author_book sətirləri silinir,
        // AuthorBook-dan Book-a cascade olmadığı üçün kitablar qalır.
        Author author = authorRepository.findByIdWithAuthorBooks(id)
                .orElseThrow(() -> new EntityNotFoundException("Author not found: " + id));
        authorRepository.delete(author);
    }

    private Author findAuthor(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Author not found: " + id));
    }
}
