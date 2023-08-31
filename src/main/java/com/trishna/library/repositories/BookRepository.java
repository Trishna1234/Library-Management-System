package com.trishna.library.repositories;

import com.trishna.library.models.Book;
import com.trishna.library.models.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    List<Book> findByGenre(Genre genre);
    List<Book> findByAuthorName(String authorName);
    List<Book> findByName(String bookName);
}
