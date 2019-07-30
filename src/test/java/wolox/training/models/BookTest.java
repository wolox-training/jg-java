package wolox.training.models;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;
import java.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import wolox.training.exceptions.BookAlreadyOwnedException;
import wolox.training.exceptions.BookNonOwnedException;
import wolox.training.repositories.BookRepository;
import wolox.training.repositories.UserRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class BookTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookRepository bookRepository;
    private User oneTestUser;
    private Book oneTestBook;
    private Book otherTestBook;

    @Before
    public void setUp()
        throws BookAlreadyOwnedException, NoSuchFieldException, IllegalAccessException {
        oneTestBook = new Book();
        oneTestBook.setGenre("genre");
        oneTestBook.setAuthor("author");
        oneTestBook.setTitle("title");
        oneTestBook.setImage("image");
        oneTestBook.setSubtitle("subtitle");
        oneTestBook.setPublisher("publisher");
        oneTestBook.setYear("1992");
        oneTestBook.setAges(1);
        oneTestBook.setIsbn("12345678");
        setId(1L, oneTestBook);

        otherTestBook = new Book();
        otherTestBook.setGenre("genre");
        otherTestBook.setAuthor("author");
        otherTestBook.setTitle("title2");
        otherTestBook.setImage("image");
        otherTestBook.setSubtitle("subtitle");
        otherTestBook.setPublisher("publisher");
        otherTestBook.setYear("1992");
        otherTestBook.setAges(1);
        otherTestBook.setIsbn("12345678");
        setId(1L, otherTestBook);

        oneTestUser = new User();
        oneTestUser.setUsername("username");
        oneTestUser.setName("name");
        oneTestUser.setBirthdate(LocalDate.of(1990, 10, 10));
        oneTestUser.addBook(oneTestBook);
        setId(1L, oneTestUser);
    }

    public static void setId(Long id, Object object)
        throws NoSuchFieldException, IllegalAccessException {
        Field fieldId = object.getClass().getDeclaredField("id");
        fieldId.setAccessible(true);
        fieldId.set(object, id);
    }


    @Test
    public void whenCreateBook_ThenBookIsPersisted(){
        Book persistedBook = bookRepository.findByTitle("title").orElse(new Book());
        assertEquals(persistedBook.getGenre(), persistedBook.getGenre());
        assertEquals(persistedBook.getAuthor(), persistedBook.getAuthor());
        assertEquals(persistedBook.getTitle(), persistedBook.getTitle());
        assertEquals(persistedBook.getImage(), persistedBook.getImage());
        assertEquals(persistedBook.getSubtitle(), persistedBook.getSubtitle());
        assertEquals(persistedBook.getPublisher(), persistedBook.getPublisher());
        assertEquals(persistedBook.getYear(), persistedBook.getYear());
        assertEquals(persistedBook.getAges(), persistedBook.getAges());
        assertEquals(persistedBook.getIsbn(), persistedBook.getIsbn());


    }

    @Test(expected = NullPointerException.class)
    public void whenCreateUserWithOutTitle_ThenThrowException(){
        oneTestBook.setTitle(null);
        bookRepository.save(oneTestBook);
    }

    @Test
    public void whenAddBookToExistingUser_ThenBookIsAdded() throws BookAlreadyOwnedException {
        oneTestUser.addBook(otherTestBook);
        assertEquals(oneTestUser.getBooks().size(),2);
    }
}
