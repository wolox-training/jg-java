package wolox.training.models;

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
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UserTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;
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
        bookRepository.save(oneTestBook);

        otherTestBook = new Book();
        otherTestBook.setGenre("genre");
        otherTestBook.setAuthor("author");
        otherTestBook.setTitle("title");
        otherTestBook.setImage("image");
        otherTestBook.setSubtitle("subtitle");
        otherTestBook.setPublisher("publisher");
        otherTestBook.setYear("1992");
        otherTestBook.setAges(1);
        otherTestBook.setIsbn("123456789");
        bookRepository.save(otherTestBook);

        oneTestUser = new User();
        oneTestUser.setUsername("username");
        oneTestUser.setName("name");
        oneTestUser.setBirthdate(LocalDate.of(1990, 10, 10));
        oneTestUser.addBook(oneTestBook);
        userRepository.save(oneTestUser);
    }

    public static void setId(Long id, Object object)
        throws NoSuchFieldException, IllegalAccessException {
        Field fieldId = object.getClass().getDeclaredField("id");
        fieldId.setAccessible(true);
        fieldId.set(object, id);
    }


    @Test
    public void whenCreateUser_ThenUserIsPersisted(){
        User persistedUser = userRepository.findByUsername("username").orElse(new User());
        assertEquals(persistedUser.getUsername(), oneTestUser.getUsername());
        assertEquals(persistedUser.getName(), oneTestUser.getName());
        assertEquals(persistedUser.getBirthdate(), oneTestUser.getBirthdate());
        assertEquals(persistedUser.getBooks().size(), oneTestUser.getBooks().size());
    }

    @Test(expected = NullPointerException.class)
    public void whenCreateUserWithOutUsername_ThenThrowException(){
        oneTestUser.setName(null);
        userRepository.save(oneTestUser);
    }

    @Test
    public void whenAddBookToExistingUser_ThenBookIsAdded() throws BookAlreadyOwnedException {
        oneTestUser.addBook(otherTestBook);
        assertEquals(oneTestUser.getBooks().size(),2);
    }

    @Test(expected = BookAlreadyOwnedException.class)
    public void whenAddBookAlreadyOwned_ThenThrowException() throws BookAlreadyOwnedException {
        oneTestUser.addBook(oneTestBook);
    }

    @Test
    public void whenDeleteBookToExistingUser_ThenBookIsDeleted()
        throws BookNonOwnedException {
        oneTestUser.deleteBook(oneTestBook);
        assertEquals(oneTestUser.getBooks().size(),0);
    }

    @Test(expected = BookNonOwnedException.class)
    public void whenDeleteBookNonOwned_ThenThrowException() throws BookNonOwnedException {
        oneTestUser.deleteBook(otherTestBook);
    }

}
