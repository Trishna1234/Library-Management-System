package com.trishna.library.dtos;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetAdminResponse {
    private Integer id;
    private String name;
    private String email;
    private Date createdOn;
    private String username;
}
