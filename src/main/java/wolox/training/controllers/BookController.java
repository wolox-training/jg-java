package wolox.training.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
import wolox.training.exceptions.BookIdMismatchException;
import wolox.training.exceptions.BookNotFoundException;
import wolox.training.models.Book;
import wolox.training.repositories.BookRepository;

@Api
@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @GetMapping("/title/{bookTitle}")
    @ApiOperation(value = "given a title return a book", response = Book.class)
    @ApiResponses(
        value = {@ApiResponse(code = 200, message = "Book found"),
            @ApiResponse(code = 404, message = "Book not found")
        }
    )
    public Book findByTitle(@ApiParam(value = "book title", required = true) @PathVariable String bookTitle) throws BookNotFoundException {
        return bookRepository.findByTitle(bookTitle).orElseThrow(BookNotFoundException::new);
    }

    @PostMapping
    @ApiOperation(value = "create a book", response = Book.class)
    @ApiResponses(
        value = {@ApiResponse(code = 201, message = "Book created")
        }
    )
    @ResponseStatus(HttpStatus.CREATED)
    public Book create(@RequestBody Book book) {
        return bookRepository.save(book);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "delete a book", response = Book.class)
    @ApiResponses(
        value = {@ApiResponse(code = 200, message = "Book deleted"),
            @ApiResponse(code = 404, message = "Book not found")
        }
    )
    public void delete(@ApiParam(value = "book id", required = true) @PathVariable Long id) throws BookNotFoundException {
        bookRepository.findById(id)
            .orElseThrow(BookNotFoundException::new);
        bookRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "update a book", response = Book.class)
    @ApiResponses(
        value = {@ApiResponse(code = 200, message = "Book updated"),
            @ApiResponse(code = 404, message = "Book not found"),
            @ApiResponse(code = 400, message = "Bad id")
        }
    )
    public Book update(@RequestBody Book book, @ApiParam(value = "book id", required = true) @PathVariable Long id)
        throws BookIdMismatchException, BookNotFoundException {
        if (!book.getId().equals(id))
            throw new BookIdMismatchException();
        bookRepository.findById(id)
            .orElseThrow(BookNotFoundException::new);
        return bookRepository.save(book);
    }
}
