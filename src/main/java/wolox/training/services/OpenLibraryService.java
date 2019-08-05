package wolox.training.services;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wolox.training.models.Book;
import wolox.training.repositories.BookRepository;

@Service
public class OpenLibraryService {

    @Autowired
    private BookRepository bookRepository;

    private static final String TITLE = "title";
    private static final String SUBTITLE = "subtitle";
    private static final String PUBLISHER = "publishers";
    private static final String PUBLISHER_DATE = "publish_date";
    private static final String NAME = "name";
    private static final String AUTHOR = "authors";
    private static final String IMAGE = "cover";
    private static final String SMALL = "small";
    private static final String GENRE = "subjects";

    public Optional<Book> bookInfo(String isbn) throws Exception {
        try {
            String json = getJSON("https://openlibrary.org/api/books?bibkeys=ISBN:"+ isbn +"&format=json&jscmd=data");
            JSONObject obj = new JSONObject(json);
            obj = obj.getJSONObject("ISBN:"+isbn);
            Book book = new Book();
            book.setIsbn(isbn);
            book.setTitle(obj.getString(TITLE));
            book.setSubtitle(obj.getString(SUBTITLE));
            book.setPublisher(obj.getJSONArray(PUBLISHER).getJSONObject(0).getString(NAME));
            book.setYear(obj.getString(PUBLISHER_DATE));
            book.setAuthor(obj.getJSONArray(AUTHOR).getJSONObject(0).getString(NAME));
            book.setImage(obj.getJSONObject(IMAGE).getString(SMALL));
            book.setGenre(obj.getJSONArray(GENRE).getJSONObject(0).getString(NAME));
            book.setAges(1);
            bookRepository.save(book);
            return Optional.of(book);
        } catch (Exception e){
            return Optional.empty();
        }
    }


    private static String getJSON(String urlToRead) throws Exception {
        StringBuilder result = new StringBuilder();
        URL url = new URL(urlToRead);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        rd.close();
        return result.toString();
    }


}
