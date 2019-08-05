package wolox.training.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import wolox.training.exceptions.BookAlreadyOwnedException;
import wolox.training.models.Book;
import wolox.training.models.User;
import wolox.training.repositories.BookRepository;
import wolox.training.repositories.UserRepository;

@RunWith(SpringRunner.class)
@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository mockUserRepository;
    @MockBean
    private BookRepository mockBookRepository;

    private User oneTestUser;
    private Book oneTestBook;

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

        oneTestUser = new User();
        oneTestUser.setUsername("username");
        oneTestUser.setName("name");
        oneTestUser.setBirthdate(LocalDate.of(1990, 10, 10));
        oneTestUser.addBook(oneTestBook);
        setId(1L, oneTestUser);
    }

    @Test
    public void whenFindByTitleWhichExists_thenBookIsReturned() throws Exception {
        Mockito.when(mockBookRepository.findByTitle(oneTestBook.getTitle()))
            .thenReturn(Optional.of(oneTestBook));
        mockMvc.perform(get("/api/books/title/title").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(
                "{\"id\": 1,\"genre\": \"genre\",\"author\":"
                    + " \"author\",\"title\": \"title\",\"image\": \"image\",\"subtitle\": \"subtitle\","
                    + "\"publisher\": \"publisher\",\"year\": \"1992\",\"ages\": 1,\"isbn\": \"12345678\"}"
            ));
    }

    @Test
    public void whenFindByTitleWhichNotExists_then404IsReturned() throws Exception {
        mockMvc.perform(get("/api/books/title/otherTitle").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is4xxClientError());
    }

    private static void setId(Long id, Object object)
        throws NoSuchFieldException, IllegalAccessException {
        Field fieldId = object.getClass().getDeclaredField("id");
        fieldId.setAccessible(true);
        fieldId.set(object, id);
    }
}
