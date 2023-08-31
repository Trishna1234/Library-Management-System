package com.trishna.library.dtos;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetStudentResponse {
    private Integer id;
    private String name;
    private String email;
    private Integer age;
    private Date createdOn;
    private Date updatedOn;
    private String username;

}
