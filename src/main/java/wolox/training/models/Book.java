package wolox.training.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;

@Entity
@ApiModel(description = "Books model")
public class Book {

    public Book() {
        // default
    }

    @Id
    @GeneratedValue (strategy = GenerationType.SEQUENCE, generator = "BOOK_SEQ")
    @SequenceGenerator(name = "BOOK_SEQ", sequenceName = "BOOK_SEQ")
    private Long id;

    @ApiModelProperty(notes = "genre", required = true, example = "Horror")
    @Column(nullable = false)
    @NotNull
    private String genre;

    @ApiModelProperty(notes = "author", required = true, example = "Stephen King")
    @Column(nullable = false)
    @NotNull
    private String author;

    @ApiModelProperty(notes = "title", required = true, example = "IT")
    @Column(nullable = false)
    @NotNull
    private String title;

    @ApiModelProperty(notes = "image", required = true, example = "url")
    @Column(nullable = false)
    @NotNull
    private String image;

    @ApiModelProperty(notes = "subtitle", required = true)
    @Column(nullable = false)
    @NotNull
    private String subtitle;

    @ApiModelProperty(notes = "publisher", required = true)
    @Column(nullable = false)
    @NotNull
    private String publisher;

    @ApiModelProperty(notes = "year", required = true, example = "1986")
    @Column(nullable = false)
    @NotNull
    private String year;

    @ApiModelProperty(notes = "ages", required = true)
    @Column(nullable = false)
    @NotNull
    private Integer ages;

    @ApiModelProperty(notes = "isbn", required = true)
    @Column(nullable = false, unique = true)
    @NotNull
    private String isbn;

    @ApiModelProperty(notes = "list of users")
    @JsonIgnore
    @ManyToMany(mappedBy = "books")
    private Set<User> user = new HashSet<>();

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Integer getAges() {
        return ages;
    }

    public void setAges(Integer ages) {
        this.ages = ages;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Long getId() {
        return id;
    }

    public Set<User> getUser() {
        return (Set<User>) Collections.unmodifiableSet(user);
    }
}
