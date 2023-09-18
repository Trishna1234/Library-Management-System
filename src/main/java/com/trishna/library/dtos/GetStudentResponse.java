package com.trishna.library.dtos;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetStudentResponse implements Serializable {
    private Integer id;
    private String name;
    private String email;
    private Integer age;
    private Date createdOn;
    private Date updatedOn;
    private String username;

}
