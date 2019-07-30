package wolox.training.models;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;
import wolox.training.exceptions.BookAlreadyOwnedException;
import wolox.training.exceptions.BookNonOwnedException;

@Entity(name="Users")
public class User {
    public User() {
        // default
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_SEQ")
    @SequenceGenerator(name = "USER_SEQ", sequenceName = "USER_SEQ")
    private Long id;

    @Column(nullable = false, unique = true)
    @NotNull
    private String username;

    @Column(nullable = false)
    @NotNull
    private String name;

    @Column(nullable = false)
    @NotNull
    private LocalDate birthdate;

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Book> books = new HashSet<>();

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public Set<Book> getBooks() {
        return (Set<Book>) Collections.unmodifiableSet(books);
    }

    public Book addBook(Book book) throws BookAlreadyOwnedException {
        if(books.contains(book))
            throw new BookAlreadyOwnedException();
        books.add(book);
        return book;
    }

    public void deleteBook(Book book) throws BookNonOwnedException {
        if(!books.contains(book))
            throw new BookNonOwnedException();
        books.remove(book);
    }

    public Long getId() {
        return id;
    }
}
