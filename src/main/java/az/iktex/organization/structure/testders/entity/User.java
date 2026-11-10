package az.iktex.organization.structure.testders.entity;

import az.iktex.organization.structure.testders.entity.converter.GenderConverter;
import az.iktex.organization.structure.testders.enums.Gender;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users",
        uniqueConstraints = @UniqueConstraint(name = "uk_user_email", columnNames = "email"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "surname", nullable = false, length = 50)
    private String surname;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Convert(converter = GenderConverter.class)
    @Column(name = "gender_id", nullable = false)
    private Gender gender;

    // mappedBy = "user": əlaqə sütunu (user_id) passports cədvəlindədir, ona görə Passport owner tərəfdir.
    // cascade = ALL: passport user ilə birlikdə yaranır və silinir, ayrıca service/repository save tələb etmir.
    // orphanRemoval = true: user.setPassport(null) edəndə sahibsiz qalan passport bazadan silinir.
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Passport passport;

    // İki tərəfi sinxron saxlayır: yalnız User-də dəyişsək, owner tərəfdə (Passport.user) user_id yazılmaz.
    public void setPassport(Passport passport) {
        this.passport = passport;
        if (passport != null) {
            passport.setUser(this);
        }
    }
}
