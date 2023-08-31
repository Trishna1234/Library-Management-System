package com.trishna.library.dtos;

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
//    private Integer studentId;
//    private String studentName;
//    private String studentEmail;
}
