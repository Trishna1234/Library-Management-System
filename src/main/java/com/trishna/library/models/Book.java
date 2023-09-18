package com.trishna.library.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.trishna.library.dtos.GetBookResponse;
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
    private String name;
    @Enumerated(value = EnumType.STRING)
    private Genre genre;
    @CreationTimestamp
    private Date createdOn;
    @UpdateTimestamp
    private Date updatedOn;
    @Enumerated(EnumType.STRING)
    private BookStatus status;
    @ManyToOne
    @JoinColumn
    @JsonIgnoreProperties({"bookList"})
    private Author author;
    @ManyToOne
    @JoinColumn
    @JsonIgnoreProperties({"bookList"})
    private Student student;
    @OneToMany(mappedBy = "book")
    @JsonIgnoreProperties({"book"})
    private List<Transaction> transactionList;

    public GetBookResponse to(){
        return GetBookResponse.builder()
                .id(this.id)
                .name(this.name)
                .genre(this.genre)
                .createdOn(this.createdOn)
                .updatedOn((this.updatedOn))
                .authorId(this.author.getId())
                .authorEmail(this.author.getEmail())
                .authorName(this.author.getName())
                .status(this.status)
                .build();
    }

}
