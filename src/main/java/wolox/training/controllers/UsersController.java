package wolox.training.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import wolox.training.exceptions.BookAlreadyOwnedException;
import wolox.training.exceptions.BookIdMismatchException;
import wolox.training.exceptions.BookNonOwnedException;
import wolox.training.exceptions.BookNotFoundException;
import wolox.training.exceptions.UserIdMismatchException;
import wolox.training.exceptions.UserNotFoundException;
import wolox.training.models.Book;
import wolox.training.models.Users;
import wolox.training.repositories.BookRepository;
import wolox.training.repositories.UsersRepository;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private BookRepository bookRepository;

    @GetMapping("/username/{username}")
    public Users findByUsername(@PathVariable String username) throws UserNotFoundException {
        return usersRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
    }

    @GetMapping("{id}")
    public Users get(@PathVariable Long id) throws UserNotFoundException {
        return usersRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Users create(@RequestBody Users users) {
        return usersRepository.save(users);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) throws UserNotFoundException {
        usersRepository.findById(id)
            .orElseThrow(UserNotFoundException::new);
        usersRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    public Users update(@RequestBody Users user, @PathVariable Long id)
        throws UserIdMismatchException, UserNotFoundException {
        if (user.getId() != id)
            throw new UserIdMismatchException();
        usersRepository.findById(id)
            .orElseThrow(UserNotFoundException::new);
        return usersRepository.save(user);
    }

    @PutMapping("/{id}/books/{book_id}")
    public Users addBook(@PathVariable("book_id") Long bookId, @PathVariable("id") Long id)
        throws BookNotFoundException, UserNotFoundException, BookAlreadyOwnedException {
        Users user = usersRepository.findById(id).orElseThrow(UserNotFoundException::new);
        Book book = bookRepository.findById(bookId).orElseThrow(BookNotFoundException::new);
        user.addBook(book);
        return usersRepository.save(user);
    }

    @DeleteMapping("/{id}/books/{book_id}")
    public Users deleteBook(@PathVariable("book_id") Long bookId, @PathVariable("id") Long id)
        throws BookNotFoundException, UserNotFoundException, BookNonOwnedException {
        Users user = usersRepository.findById(id).orElseThrow(UserNotFoundException::new);
        Book book = bookRepository.findById(bookId).orElseThrow(BookNotFoundException::new);
        user.deleteBook(book);
        return usersRepository.save(user);
    }
}
