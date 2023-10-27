package com.trishna.library.dtos;

import com.trishna.library.models.Author;
import com.trishna.library.models.Book;
import com.trishna.library.models.utils.Genre;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateBookRequest {
    @NotBlank
    private String name;
    @NotNull
    private Genre genre;
    @NotNull
    private Integer quantity;
    private List<Author> authorList;
    public Book to(){
        return Book.builder()
                .name(this.name)
                .genre(this.genre)
                .quantity(this.quantity)
                .authorList(this.authorList)
                .build();
    }
}
