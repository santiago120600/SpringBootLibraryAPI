import com.api.library.repository.LibraryRepository;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.api.library.model.Library;
import com.api.library.repository.LibraryRepositoryCustom;

public class LibraryRepositoryImpl implements LibraryRepositoryCustom {
    
    @Autowired
    LibraryRepository repository;

    public List<Library> findByAuthor(String author){
        List<Library> books = repository.findAll();
        List<Library> booksMatching = new ArrayList<Library>();
        for(Library book: books){
            if(book.getAuthor().equalsIgnoreCase(author)){
               booksMatching.add(book); 
            }
        }
        return booksMatching;
    }

}
