package wolox.training.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wolox.training.exceptions.BadSortException;
import wolox.training.exceptions.BookIdMismatchException;
import wolox.training.exceptions.BookNotFoundException;
import wolox.training.models.Book;
import wolox.training.models.User;
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

    private static final String[] array = {"genre", "author", "title", "image", "subtitle", "publisher", "year", "ages", "id"};
    private static final LinkedList permittedSort = new LinkedList(Arrays.asList(array));


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
            @ApiResponse(code = 404, message = "Bad id")
        }
    )
    public Book update(@RequestBody Book book, @ApiParam(value = "book id", required = true) @PathVariable Long id)
        throws BookIdMismatchException, BookNotFoundException {
        if (book.getId().equals(id))
            throw new BookIdMismatchException();
        bookRepository.findById(id)
            .orElseThrow(BookNotFoundException::new);
        return bookRepository.save(book);
    }

    @GetMapping
    public List<Book> getAll(@RequestParam(required = false) String genre, @RequestParam(required = false) String author, @RequestParam(required = false) String title,
                             @RequestParam(required = false) String image, @RequestParam(required = false) String subtitle, @RequestParam(required = false) String publisher,
                             @RequestParam(required = false) String year, @RequestParam(required = false) String isbn, @RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "id") String sort ) throws BadSortException {
        if(!permittedSort.contains(sort))
            throw new BadSortException();
        Pageable pageable =
                PageRequest.of(page, 10, Sort.by(sort));
         return bookRepository.
                findAll(genre,author,title,image,subtitle,publisher,year,isbn, pageable);
    }
}
