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
    private Book oneTestBook;
    private Book otherTestBook;

    @Before
    public void setUp()
            throws BookAlreadyOwnedException, NoSuchFieldException, IllegalAccessException {
        oneTestBook = new Book();
        oneTestBook.setGenre("genre");
        oneTestBook.setAuthor("author");
        oneTestBook.setTitle("fake_title");
        oneTestBook.setImage("image");
        oneTestBook.setSubtitle("subtitle");
        oneTestBook.setPublisher("publisher");
        oneTestBook.setYear("1992");
        oneTestBook.setAges(1);
        oneTestBook.setIsbn("234");
        bookRepository.save(oneTestBook);

        otherTestBook = new Book();
        otherTestBook.setGenre("genre");
        otherTestBook.setAuthor("author");
        otherTestBook.setTitle("title2");
        otherTestBook.setImage("image");
        otherTestBook.setSubtitle("subtitle");
        otherTestBook.setPublisher("publisher");
        otherTestBook.setYear("1992");
        otherTestBook.setAges(1);
        otherTestBook.setIsbn("2345");
        bookRepository.save(otherTestBook);
    }

    private static void setId(Long id, Object object)
        throws NoSuchFieldException, IllegalAccessException {
        Field fieldId = object.getClass().getDeclaredField("id");
        fieldId.setAccessible(true);
        fieldId.set(object, id);
    }


    @Test
    public void whenCreateBook_ThenBookIsPersisted(){
        Book persistedBook = bookRepository.findByTitle("fake_title").orElse(new Book());
        assertEquals(persistedBook.getGenre(), oneTestBook.getGenre());
        assertEquals(persistedBook.getAuthor(), oneTestBook.getAuthor());
        assertEquals(persistedBook.getTitle(), oneTestBook.getTitle());
        assertEquals(persistedBook.getImage(), oneTestBook.getImage());
        assertEquals(persistedBook.getSubtitle(), oneTestBook.getSubtitle());
        assertEquals(persistedBook.getPublisher(), oneTestBook.getPublisher());
        assertEquals(persistedBook.getYear(), oneTestBook.getYear());
        assertEquals(persistedBook.getAges(), oneTestBook.getAges());
        assertEquals(persistedBook.getIsbn(), oneTestBook.getIsbn());
    }

    @Test(expected = NullPointerException.class)
    public void whenCreateBookWithOutTitle_ThenThrowException(){
        oneTestBook.setTitle(null);
        bookRepository.save(oneTestBook);
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenCreateBookWithAgeNegative_ThenThrowException(){
        oneTestBook.setAges(-5);
        bookRepository.save(oneTestBook);
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenCreateBookWithNonNumericIsbn_ThenThrowException(){
        oneTestBook.setIsbn("isbn");
        bookRepository.save(oneTestBook);
    }

}
