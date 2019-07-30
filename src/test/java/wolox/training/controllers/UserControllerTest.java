package wolox.training.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Optional;
import javafx.application.Application;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import wolox.training.TrainingApplication;
import wolox.training.exceptions.BookAlreadyOwnedException;
import wolox.training.models.Book;
import wolox.training.models.User;
import wolox.training.repositories.BookRepository;
import wolox.training.repositories.UserRepository;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

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
    public void whenFindByUsernameWhichExists_thenUserIsReturned() throws Exception {
        Mockito.when(mockUserRepository.findByUsername(oneTestUser.getUsername()))
            .thenReturn(Optional.of(oneTestUser));
        mockMvc.perform(get("/api/users/username/username").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(
                "{ \"id\": 1,\"username\": \"username\",\"name\": \"name\",\"birthdate\":"
                    + " \"1990-10-10\",\"books\": [ {\"id\": 1,\"genre\": \"genre\",\"author\":"
                    + " \"author\",\"title\": \"title\",\"image\": \"image\",\"subtitle\": \"subtitle\","
                    + "\"publisher\": \"publisher\",\"year\": \"1992\",\"ages\": 1,\"isbn\": \"12345678\" }] }"
            ));
    }

    @Test
    public void whenFindByUsernameWhichNoyExists_then404IsReturned() throws Exception {
        mockMvc.perform(get("/api/users/username/username").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is4xxClientError());
    }

    public static void setId(Long id, Object object)
        throws NoSuchFieldException, IllegalAccessException {
        Field fieldId = object.getClass().getDeclaredField("id");
        fieldId.setAccessible(true);
        fieldId.set(object, id);
    }
}



