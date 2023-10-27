package com.trishna.library.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.trishna.library.dtos.GetBookResponse;
import com.trishna.library.models.utils.Genre;
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
public class Book implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true, nullable = false)
    private String name;
    @Enumerated(value = EnumType.STRING)
    private Genre genre;
    private Integer quantity;
    @CreationTimestamp
    private Date createdOn;
    @UpdateTimestamp
    private Date updatedOn;
    @ManyToMany
    private List<Author> authorList;
    @ManyToMany(mappedBy = "bookList")
//    @JoinColumn
    @JsonIgnoreProperties({"bookList"})
    private List<Student> studentList;
    @OneToMany(mappedBy = "book")
    @JsonIgnoreProperties({"book"})
    private List<Transaction> transactionList;

    public GetBookResponse to(){
        return GetBookResponse.builder()
                .id(this.id)
                .name(this.name)
                .genre(this.genre)
                .quantity(this.quantity)
                .createdOn(this.createdOn)
                .updatedOn((this.updatedOn))
                .build();
    }

}
