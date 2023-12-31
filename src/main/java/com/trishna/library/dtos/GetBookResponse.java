package com.trishna.library.dtos;

import com.trishna.library.models.Book;
import com.trishna.library.models.utils.Genre;
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
    private Integer quantity;
    private Date createdOn;
    private Date updatedOn;

    public Book to(){
        return Book.builder()
                .id(this.id)
                .name(this.name)
                .genre(this.genre)
                .quantity(this.quantity)
                .createdOn(this.createdOn)
                .updatedOn(this.updatedOn)
                .build();
    }
}
