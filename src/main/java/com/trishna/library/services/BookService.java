package com.trishna.library.services;

import com.trishna.library.dtos.GetBookResponse;
import com.trishna.library.dtos.UpdateRequest;
import com.trishna.library.exceptions.Book.BookNotFoundException;
import com.trishna.library.exceptions.GeneralException;
import com.trishna.library.models.*;
import com.trishna.library.models.utils.Genre;
import com.trishna.library.repositories.BookRepository;
import com.trishna.library.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    @Autowired
    BookRepository bookRepository;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    AuthorService authorService;


    public void createOrUpdate(Book book){
        List<Author> bookAuthor = book.getAuthorList();
        List<Author> retrievedAuthor = new ArrayList<>();
        for(Author author : bookAuthor){
            retrievedAuthor.add(authorService.getOrCreate(author));
        }
        book.setAuthorList(retrievedAuthor);
        bookRepository.save(book);
    }
    public void update(Book book){
        bookRepository.save(book);
    }

    public Book findById(Integer id){
        Optional<Book> op = bookRepository.findById(id);
        if(op.isPresent())
            return op.get();
        else return null;
    }


    public List<GetBookResponse> findBook(String searchKey, String searchValue) throws Exception {
        switch (searchKey){
            case "id": {
                Optional<Book> book = bookRepository.findById(Integer.parseInt(searchValue));
//                Student student = book.get().getStudent();
                if(book.isPresent()){
                    return Arrays.asList(book.get().to());
                }else{
                    return new ArrayList<>();
                }
            }
            case "genre": {
                List<Book> list = bookRepository.findByGenre(Genre.valueOf(searchValue));
                List<GetBookResponse> result = new ArrayList<>();
                for(int i=0;i< list.size();i++){
                    result.add(list.get(i).to());
                }
                return result;
            }
            case "author_name":{
                List<Book> list = authorService.findBookByName(searchValue);
                List<GetBookResponse> result = new ArrayList<>();
                for(int i=0;i< list.size();i++){
                    result.add(list.get(i).to());
                }
                return result;
            }
            case "book_name":{
                List<Book> list = bookRepository.findByName(searchValue);
                List<GetBookResponse> result = new ArrayList<>();
                for(int i=0;i< list.size();i++){
                    result.add(list.get(i).to());
                }
                return result;
            }
            default:
                throw new GeneralException("Search key not valid " + searchKey);
        }
    }

    public void updateBook(Integer bookId, UpdateRequest request) throws Exception {
        String updateKey = request.getUpdateKey();
        String updateValue = request.getUpdateValue();
        Book retrievedBook = bookRepository.findById(bookId).orElse(null);
        if(retrievedBook == null) throw new BookNotFoundException("Enter valid Book Id");
        switch (updateKey){
            case "name":{
                retrievedBook.setName(updateValue);
                break;
            }
            case "quantity":{
                retrievedBook.setQuantity(Integer.valueOf(updateValue));
                break;
            }
            case "genre": {
                retrievedBook.setGenre(Genre.valueOf(updateValue));
                break;
            }
            default:
                throw new GeneralException("Invalid update key");
        }
        bookRepository.save(retrievedBook);
    }

    public void deleteBook(int bookId){
        Book book = bookRepository.findById(bookId).orElse(null);
        if(book == null) throw new BookNotFoundException("Enter valid Book Id");
        List<Transaction> list = transactionRepository.findByBook(book);
        for (Transaction txn: list) {
            txn.setBook(null);
            transactionRepository.save(txn);
        }
        bookRepository.deleteById(bookId);
    }
}
