package wolox.training.repositories;

import java.util.List;
import java.util.Optional;
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

    @Query("select b from Book b where ( b.publisher = ?1 or ?1 is null ) and ( b.genre = ?2 or ?2 is null) and ( b.year = ?3 or ?3 is null )")
    List<Book> findByPublisherAndGenreAndYear(String publisher, String genre, String year);
}
