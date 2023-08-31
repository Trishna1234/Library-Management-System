package com.trishna.library.services;

import com.trishna.library.models.Author;
import com.trishna.library.repositories.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
