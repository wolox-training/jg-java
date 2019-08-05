package wolox.training.controllers;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import wolox.training.exceptions.BookAlreadyOwnedException;
import wolox.training.exceptions.BookNonOwnedException;
import wolox.training.exceptions.BookNotFoundException;
import wolox.training.exceptions.UserIdMismatchException;
import wolox.training.exceptions.UserNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import wolox.training.exceptions.*;
import wolox.training.models.Book;
import wolox.training.models.User;
import wolox.training.repositories.BookRepository;
import wolox.training.repositories.UserRepository;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final int SIZE = 10;

    private static final List permittedSort = Arrays.asList("name", "username", "id", "birthdate");

    @GetMapping("/username/{username}")
    public User findByUsername(@PathVariable String username) throws UserNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
    }

    @GetMapping("/{id}")
    public User get(@PathVariable Long id) throws UserNotFoundException {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody User user) {
        return userRepository.save(user);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) throws UserNotFoundException {
        userRepository.findById(id)
            .orElseThrow(UserNotFoundException::new);
        userRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    public User update(@RequestBody User user, @PathVariable Long id)
        throws UserIdMismatchException, UserNotFoundException {
        if (!user.getId().equals(id))
            throw new UserIdMismatchException();
        User u = userRepository.findById(id)
            .orElseThrow(UserNotFoundException::new);
        u.setUsername(user.getUsername());
        u.setName(user.getName());
        u.setBirthdate(user.getBirthdate());
        return userRepository.save(u);
    }

    @PutMapping("/{id}/password")
    public User update(@RequestBody String passwordBody, @PathVariable Long id, Authentication authentication)
            throws UserNotFoundException, NotAuthorizedException {
        JSONObject passwordJson = new JSONObject(passwordBody);
        String password = passwordJson.getString("password");
        User u = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
        if (!authentication.getName().equals(u.getUsername()))
            throw new NotAuthorizedException();
        u.setPassword(password);
        return userRepository.save(u);
    }

    @RequestMapping(value = "/username", method = RequestMethod.GET)
    @ResponseBody
    public User currentUserName(Authentication authentication) {
        return userRepository.findByUsername(authentication.getName()).get();
    }

    @PutMapping("/{id}/books/{book_id}")
    public User addBook(@PathVariable("book_id") Long bookId, @PathVariable("id") Long id)
        throws BookNotFoundException, UserNotFoundException, BookAlreadyOwnedException {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        Book book = bookRepository.findById(bookId).orElseThrow(BookNotFoundException::new);
        user.addBook(book);
        return userRepository.save(user);
    }

    @DeleteMapping("/{id}/books/{book_id}")
    public User deleteBook(@PathVariable("book_id") Long bookId, @PathVariable("id") Long id)
        throws BookNotFoundException, UserNotFoundException, BookNonOwnedException {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        Book book = bookRepository.findById(bookId).orElseThrow(BookNotFoundException::new);
        user.deleteBook(book);
        return userRepository.save(user);
    }

    @GetMapping
    public List<User> find(@RequestParam(required = false) String before, @RequestParam(required = false) String after, @RequestParam(required = false) String name,
                           @RequestParam(defaultValue = "id") String sort, @RequestParam(defaultValue = "0") int page) throws BadSortException {
        if(!permittedSort.contains(sort))
            throw new BadSortException();
        return userRepository.
                findByBirthdateBeforeAndAndBirthdateAfterAndNameIgnoreCaseContaining
                        (LocalDate.parse(before),LocalDate.parse(after),name, PageRequest.of(page, SIZE, Sort.by(sort)));
    }


}
