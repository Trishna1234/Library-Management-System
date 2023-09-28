package com.trishna.library.dtos;

import com.trishna.library.models.Author;
import com.trishna.library.models.Book;
import com.trishna.library.models.BookStatus;
import com.trishna.library.models.Genre;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetBookResponse {
    private Integer id;
    private String name;
    private Genre genre;
    private Date createdOn;
    private Date updatedOn;
    private Integer authorId;
    private String authorName;
    private String authorEmail;
    private BookStatus status;

    public Book to(){
        return Book.builder()
                .id(this.id)
                .name(this.name)
                .genre(this.genre)
                .createdOn(this.createdOn)
                .updatedOn(this.updatedOn)
                .author(
                        Author.builder()
                                .id(this.authorId)
                                .name(this.authorName)
                                .email(this.authorEmail)
                                .build()
                )
                .build();
    }
}
