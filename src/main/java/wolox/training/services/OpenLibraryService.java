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

    public Optional<Book> bookInfo(String isbn) throws Exception {
        try {
            String json = getJSON("https://openlibrary.org/api/books?bibkeys=ISBN:"+ isbn +"&format=json&jscmd=data");
            JSONObject obj = new JSONObject(json);
            obj = obj.getJSONObject("ISBN:"+isbn);
            Book book = new Book();
            book.setIsbn(isbn);
            book.setTitle(obj.getString("title"));
            book.setSubtitle(obj.getString("subtitle"));
            book.setPublisher(obj.getJSONArray("publishers").getJSONObject(0).getString("name"));
            book.setYear(obj.getString("publish_date"));
            book.setAuthor(obj.getJSONArray("authors").getJSONObject(0).getString("name"));
            book.setImage(obj.getJSONObject("cover").getString("small"));
            book.setGenre(obj.getJSONArray("subjects").getJSONObject(0).getString("name"));
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
