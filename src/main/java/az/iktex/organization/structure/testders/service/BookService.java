package az.iktex.organization.structure.testders.service;

import az.iktex.organization.structure.testders.entity.Author;
import az.iktex.organization.structure.testders.entity.AuthorBook;
import az.iktex.organization.structure.testders.entity.Book;
import az.iktex.organization.structure.testders.mapper.BookMapper;
import az.iktex.organization.structure.testders.repository.AuthorBookRepository;
import az.iktex.organization.structure.testders.repository.AuthorRepository;
import az.iktex.organization.structure.testders.repository.BookRepository;
import az.iktex.organization.structure.testders.request.BookRequest;
import az.iktex.organization.structure.testders.response.BookResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final AuthorBookRepository authorBookRepository;
    private final BookMapper bookMapper;

    @Transactional
    public BookResponse create(BookRequest request) {
        if (bookRepository.existsByTitle(request.title())) {
            throw new IllegalStateException("Book title already exists: " + request.title());
        }
        Book book = bookMapper.toEntity(request);
        return bookMapper.toResponse(bookRepository.save(book));
    }

    public BookResponse getById(Long id) {
        return bookMapper.toDetailResponse(findBookWithAuthors(id));
    }

    public List<BookResponse> getAll(String title) {
        List<Book> books = title == null
                ? bookRepository.findAll()
                : bookRepository.findAllByTitleContainingIgnoreCase(title);
        return bookMapper.toResponseList(books);
    }

    public List<BookResponse> getCheaperThan(BigDecimal price) {
        return bookMapper.toResponseList(bookRepository.findAllByPriceLessThanEqualOrderByPriceAsc(price));
    }

    @Transactional
    public BookResponse update(Long id, BookRequest request) {
        Book book = findBook(id);
        if (bookRepository.existsByTitleAndIdNot(request.title(), id)) {
            throw new IllegalStateException("Book title already exists: " + request.title());
        }
        bookMapper.updateEntity(request, book);
        return bookMapper.toResponse(book);
    }

    @Transactional
    public void delete(Long id) {
        bookRepository.delete(findBookWithAuthors(id));
    }

    @Transactional
    public BookResponse addAuthor(Long bookId, Long authorId) {
        Book book = findBookWithAuthors(bookId);
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author not found: " + authorId));
        if (authorBookRepository.existsByAuthorIdAndBookId(authorId, bookId)) {
            throw new IllegalStateException("Author " + authorId + " is already linked to book " + bookId);
        }
        AuthorBook authorBook = new AuthorBook();
        authorBook.setAuthor(author);
        authorBook.setBook(book);
        book.getAuthorBooks().add(authorBook);
        authorBookRepository.save(authorBook);
        return bookMapper.toDetailResponse(book);
    }

    @Transactional
    public void removeAuthor(Long bookId, Long authorId) {
        int deleted = authorBookRepository.deleteByAuthorIdAndBookId(authorId, bookId);
        if (deleted == 0) {
            throw new EntityNotFoundException("Author " + authorId + " is not linked to book " + bookId);
        }
    }

    private Book findBook(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found: " + id));
    }

    private Book findBookWithAuthors(Long id) {
        return bookRepository.findByIdWithAuthors(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found: " + id));
    }
}
