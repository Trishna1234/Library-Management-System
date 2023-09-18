package com.trishna.library.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.trishna.library.dtos.GetAdminResponse;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Admin implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @CreationTimestamp
    private Date createdOn;

    @OneToMany(mappedBy = "admin")
    @JsonIgnoreProperties({"admin"})
    private List<Transaction> transactionList;
    @OneToOne
    @JsonIgnoreProperties({"admin"})
    @JoinColumn
    private SecuredUser securedUser;

    public GetAdminResponse to(){
        return GetAdminResponse.builder()
                .id(this.id)
                .name(this.name)
                .email(this.email)
                .createdOn(this.createdOn)
                .username(this.securedUser.getUsername())
                .build();
    }

}
