package com.trishna.library.dtos;

import com.trishna.library.models.SecuredUser;
import com.trishna.library.models.Student;
import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateStudentRequest {
    @NotNull
    private String name;
    @NotNull
    private String email;
    private Integer age;
    private String username;
    private String password;


    public Student to(){
        return Student.builder()
                .name(this.name)
                .email(this.email)
                .age(this.age)
                .securedUser(
                        SecuredUser.builder()
                                .username(this.username)
                                .password(this.password)
                                .build()
                )
                .build();
    }
}
