package wolox.training.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import wolox.training.models.Book;

@Repository
public interface BookRepository extends CrudRepository<Book, Long> {

    Optional<Book> findByTitle(String title);
    Optional<Book> findByIsbn(String isbn);
    Optional<Book> findById(Long id);

    @Query("select b from Book b where ( b.genre = ?1 or ?1 is null ) and  ( b.author = ?2 or ?2 is null ) and " +
            " ( b.title = ?3 or ?3 is null ) and  ( b.image = ?4 or ?4 is null ) and ( b.subtitle = ?5 or ?5 is null ) and" +
            " ( b.publisher = ?6 or ?6 is null ) and ( b.year = ?7 or ?7 is null) and ( b.isbn = ?8 or ?8 is null )")
    List<Book> findAll(String genre, String author, String title, String image, String subtitle, String publisher, String year, String isbn, Pageable pageable);
}
