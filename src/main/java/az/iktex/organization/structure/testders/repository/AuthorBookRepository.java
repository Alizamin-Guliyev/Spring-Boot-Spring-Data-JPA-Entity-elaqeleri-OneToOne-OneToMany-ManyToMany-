package az.iktex.organization.structure.testders.repository;

import az.iktex.organization.structure.testders.entity.AuthorBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AuthorBookRepository extends JpaRepository<AuthorBook, Long> {

    // ORM (derived query)

    boolean existsByAuthorIdAndBookId(Long authorId, Long bookId);

    long countByBookId(Long bookId);

    long countByAuthorId(Long authorId);

    // JPQL

    @Modifying
    @Query("delete from AuthorBook ab where ab.author.id = :authorId and ab.book.id = :bookId")
    int deleteByAuthorIdAndBookId(@Param("authorId") Long authorId, @Param("bookId") Long bookId);

    @Query("select ab from AuthorBook ab join fetch ab.author join fetch ab.book where ab.book.id = :bookId")
    List<AuthorBook> findAllByBookIdWithAuthorAndBook(@Param("bookId") Long bookId);
}
