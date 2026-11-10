# school-api

JPA-da əsas əlaqə növlərini (OneToOne, OneToMany/ManyToOne, ManyToMany) göstərən REST API.

| Modul | Əlaqə |
| --- | --- |
| User ↔ Passport | OneToOne (iki tərəfli), `Gender` enum + `GenderConverter` |
| Department → Employee | OneToMany / ManyToOne |
| Student ↔ Course | ManyToMany, `@JoinTable` (`student_course`) |
| Author ↔ AuthorBook ↔ Book | ManyToMany, ayrıca 3-cü entity (`author_book`) |

## Texnologiyalar

Java 25, Spring Boot 4 (Web MVC, Data JPA), PostgreSQL 16 (Docker Compose), Lombok, MapStruct 1.6.3.

## İşə salmaq

1. PostgreSQL-i qaldırın (port `5433`, baza `school_api`):

   ```bash
   docker compose up -d
   ```

2. Proqramı işə salın:

   ```bash
   ./gradlew bootRun
   ```

   Windows-da: `gradlew.bat bootRun`

Proqram `http://localhost:8080` ünvanında işləyir, bütün endpoint-lər `/JPA_Entity` ilə başlayır
(`server.servlet.context-path`). `ddl-auto: create` olduğu üçün cədvəllər hər işə salınanda yenidən yaradılır.

Endpoint-ləri yoxlamaq üçün `postman/school-api.postman_collection.json` faylını Postman-a import edin.
Sorğular yuxarıdan aşağı ardıcıllıqla işlədiləndə hamısı uğurla keçir (silmə sorğuları sonuncu qovluqdadır).

## Endpoint-lər

Base URL: `http://localhost:8080/JPA_Entity`

### Users / Passport

| Metod | URL | Təsvir |
| --- | --- | --- |
| POST | `/api/users` | User yarat (email unikal) |
| GET | `/api/users/{id}` | User passport ilə birlikdə |
| GET | `/api/users/by-email?email=...` | Email ilə axtarış |
| GET | `/api/users?gender=MALE` | Hamısı; gender verilsə filter |
| GET | `/api/users/without-passport` | Passportu olmayan user-lər |
| PUT | `/api/users/{id}` | Yenilə (email başqasında varsa xəta) |
| DELETE | `/api/users/{id}` | Sil (passport da silinir) |
| POST | `/api/users/{id}/passport` | Passport əlavə et |
| GET | `/api/users/{id}/passport` | User-in passportu |
| DELETE | `/api/users/{id}/passport` | Passportu sil (orphanRemoval) |

### Departments

| Metod | URL | Təsvir |
| --- | --- | --- |
| POST | `/api/departments` | Departament yarat (ad unikal) |
| GET | `/api/departments/{id}` | İşçilər ilə birlikdə |
| GET | `/api/departments` | Hamısı (işçilərsiz) |
| GET | `/api/departments/without-employees` | İşçisi olmayanlar |
| PUT | `/api/departments/{id}` | Yenilə |
| DELETE | `/api/departments/{id}` | Sil (işçisi varsa xəta) |

### Employees

| Metod | URL | Təsvir |
| --- | --- | --- |
| POST | `/api/employees` | İşçi yarat (`departmentId` body-də) |
| GET | `/api/employees/{id}` | İşçi |
| GET | `/api/employees/department/{departmentId}` | Departamentin işçiləri |
| GET | `/api/employees/salary-range?min=500&max=2000` | Maaş aralığına görə |
| GET | `/api/employees/department/{id}/average-salary` | Orta maaş |
| PUT | `/api/employees/{id}` | Yenilə (departament dəyişə bilər) |
| DELETE | `/api/employees/{id}` | Sil (departament qalır) |

### Students

| Metod | URL | Təsvir |
| --- | --- | --- |
| POST | `/api/students` | Tələbə yarat |
| GET | `/api/students/{id}` | Kurslar ilə birlikdə |
| GET | `/api/students` | Hamısı |
| GET | `/api/students/without-courses` | Heç bir kursa yazılmayanlar |
| PUT | `/api/students/{id}` | Yenilə |
| DELETE | `/api/students/{id}` | Sil (kurslar qalır) |
| POST | `/api/students/{studentId}/courses/{courseId}` | Kursa yaz |
| DELETE | `/api/students/{studentId}/courses/{courseId}` | Kursdan çıxar |

### Courses

| Metod | URL | Təsvir |
| --- | --- | --- |
| POST | `/api/courses` | Kurs yarat (title unikal) |
| GET | `/api/courses` | Hamısı |
| GET | `/api/courses/{id}` | Biri |
| GET | `/api/courses/student/{studentId}` | Tələbənin kursları |
| GET | `/api/courses/{id}/students` | Kursun tələbələri |
| GET | `/api/courses/{id}/students/count` | Kursdakı tələbə sayı |
| PUT | `/api/courses/{id}` | Yenilə |
| DELETE | `/api/courses/{id}` | Sil (əvvəlcə tələbələrlə əlaqə qırılır) |

### Authors

| Metod | URL | Təsvir |
| --- | --- | --- |
| POST | `/api/authors` | Müəllif yarat (ad+soyad unikal) |
| GET | `/api/authors/{id}` | Müəllif |
| GET | `/api/authors?country=Azerbaijan` | Hamısı; country verilsə filter |
| GET | `/api/authors/without-books` | Kitabı olmayanlar |
| GET | `/api/authors/{id}/books` | Müəllifin kitabları |
| PUT | `/api/authors/{id}` | Yenilə |
| DELETE | `/api/authors/{id}` | Sil (kitablar qalır) |

### Books

| Metod | URL | Təsvir |
| --- | --- | --- |
| POST | `/api/books` | Kitab yarat (title unikal) |
| GET | `/api/books/{id}` | Müəlliflər ilə birlikdə |
| GET | `/api/books?title=java` | Hamısı; title verilsə axtarış |
| GET | `/api/books/cheaper-than?price=20` | Qiyməti ≤ price olanlar (artan sıra) |
| PUT | `/api/books/{id}` | Yenilə |
| DELETE | `/api/books/{id}` | Sil |
| POST | `/api/books/{bookId}/authors/{authorId}` | Kitaba müəllif bağla |
| DELETE | `/api/books/{bookId}/authors/{authorId}` | Əlaqəni sil |

POST sorğuları `201 Created`, DELETE sorğuları `204 No Content` qaytarır.
