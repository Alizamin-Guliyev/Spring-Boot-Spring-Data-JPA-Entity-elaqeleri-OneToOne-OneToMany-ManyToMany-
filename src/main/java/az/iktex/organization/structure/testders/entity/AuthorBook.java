package az.iktex.organization.structure.testders.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "author_book",
        uniqueConstraints = @UniqueConstraint(name = "uk_author_book", columnNames = {"author_id", "book_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
// ManyToMany-nin 2-ci üsulu: əlaqə cədvəli ayrıca entity-dir.
// Bunun üstünlüyü: əlaqəyə sonradan əlavə sahələr (məs. rol, tarix) yazmaq olar.
public class AuthorBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Cascade YOXDUR: əlaqə silinəndə müəllif və kitab silinməməlidir.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_ab_author"))
    private Author author;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_ab_book"))
    private Book book;
}
