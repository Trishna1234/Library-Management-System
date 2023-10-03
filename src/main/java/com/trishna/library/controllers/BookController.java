package com.trishna.library.controllers;

import com.trishna.library.dtos.CreateBookRequest;
import com.trishna.library.dtos.GetBookResponse;
import com.trishna.library.dtos.SearchBookRequest;
import com.trishna.library.dtos.UpdateRequest;
import com.trishna.library.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class BookController {
    @Autowired
    BookService bookService;
    @PostMapping("/book")
    public void create(@RequestBody @Valid CreateBookRequest createBookRequest){
        bookService.createOrUpdate(createBookRequest.to());
    }
    @GetMapping("/book")
    public List<GetBookResponse> getBooks(@RequestBody @Valid SearchBookRequest searchBookRequest) throws Exception {
        return bookService.findBook(
                searchBookRequest.getSearchKey(),
                searchBookRequest.getSearchValue()
        );
    }

    @PatchMapping("/update/book-by-id/{bookId}")
    public void updateBooks(@PathVariable Integer bookId, @RequestBody UpdateRequest request) throws Exception {
        bookService.updateBook(bookId, request);
    }

    @DeleteMapping("/deleteBook/{bookId}")
    public void deleteBook(@PathVariable Integer bookId){
        bookService.deleteBook(bookId);
    }
}
