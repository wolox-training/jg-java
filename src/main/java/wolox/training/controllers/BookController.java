package wolox.training.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wolox.training.exceptions.BookIdMismatchException;
import wolox.training.exceptions.BookNotFoundException;
import wolox.training.models.Book;
import wolox.training.repositories.BookRepository;
import wolox.training.services.OpenLibraryService;

@Api
@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private OpenLibraryService openLibraryService;

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

    @GetMapping("/isbn/{bookIsbn}")
    @ApiOperation(value = "given a isbn return a book", response = Book.class)
    @ApiResponses(
        value = {@ApiResponse(code = 200, message = "Book found"),
            @ApiResponse(code = 201, message = "Book created"),
            @ApiResponse(code = 404, message = "Book not found")
        }
    )
    public ResponseEntity<Book> findByIsbn(@ApiParam(value = "book isbn", required = true) @PathVariable String bookIsbn)
        throws Exception {
         Optional<Book> book = bookRepository.findByIsbn(bookIsbn);
         if (!book.isPresent()){
             return  ResponseEntity
                 .status(HttpStatus.CREATED).body(openLibraryService.bookInfo(bookIsbn).orElseThrow(BookNotFoundException::new));
         }
         return  ResponseEntity.status(HttpStatus.OK).body(book.get());
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

    @GetMapping
    public List<Book> find(@RequestParam(required = false) String publisher, @RequestParam(required = false) String genre, @RequestParam(required = false) String year) {
        return bookRepository.findByPublisherAndGenreAndYear(publisher,genre,year);
    }
}
