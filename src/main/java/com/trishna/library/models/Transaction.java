package com.trishna.library.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Transaction implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String txnId;
    @Enumerated(value = EnumType.STRING)
    private TransactionType transactionType;
    @Enumerated(value = EnumType.STRING)
    private TransactionStatus transactionStatus;
    @CreationTimestamp
    private Date createdOn;
    @UpdateTimestamp
    private Date updatedOn;
    private Integer fine;
    @ManyToOne
    @JoinColumn
    @JsonIgnoreProperties({"transactionList"})
    private Book book;
    @ManyToOne
    @JoinColumn
    @JsonIgnoreProperties({"transactionList"})
    private Student student;
    @ManyToOne
    @JoinColumn
    @JsonIgnoreProperties("transactionList")
    private Admin admin;
}
