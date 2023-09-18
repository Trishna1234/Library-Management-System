package com.trishna.library.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.trishna.library.dtos.GetStudentResponse;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Student implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @Column(unique = true, nullable = false)
    private String email;
    private Integer age;
    @CreationTimestamp
    private Date createdOn;
    @UpdateTimestamp
    private Date updatedOn;
    @OneToMany(mappedBy = "student")
    @JsonIgnoreProperties({"student"})
    private List<Book> bookList;
    @OneToMany(mappedBy = "student")
    @JsonIgnoreProperties({"student"})
    private List<Transaction> transactionList;
    @OneToOne
    @JsonIgnoreProperties({"student"})
    @JoinColumn
    private SecuredUser securedUser;

    public GetStudentResponse to(){
        return GetStudentResponse.builder()
                .id(this.id)
                .name(this.name)
                .email(this.email)
                .age(this.age)
                .createdOn(this.createdOn)
                .updatedOn(this.updatedOn)
                .username(this.securedUser.getUsername())
                .build();
    }

}
