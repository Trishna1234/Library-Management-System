package com.trishna.library.services;

import com.trishna.library.models.Author;
import com.trishna.library.models.Book;
import com.trishna.library.repositories.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {
    @Autowired
    AuthorRepository authorRepository;
    public Author getOrCreate(Author author){
        Author retrievedAuthor = authorRepository.findByEmail(author.getEmail());
        if(retrievedAuthor == null)
            retrievedAuthor = authorRepository.save(author);
        return retrievedAuthor;
    }

    public List<Book> findBookByName(String name) {
        return authorRepository.findByName(name).getBookList();
    }
}
