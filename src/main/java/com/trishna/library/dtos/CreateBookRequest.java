package com.trishna.library.dtos;

import com.trishna.library.models.Author;
import com.trishna.library.models.Book;
import com.trishna.library.models.Genre;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
    @NotBlank
    private String authorName;
    @NotBlank
    private String authorEmail;
    public Book to(){
        return Book.builder()
                .name(this.name)
                .genre(this.genre)
                .quantity(this.quantity)
                .author(
                        Author.builder()
                                .name(this.authorName)
                                .email(this.authorEmail)
                                .build()
                )
                .build();
    }
}
