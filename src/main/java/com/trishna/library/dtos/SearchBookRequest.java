package com.trishna.library.dtos;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchBookRequest {
    @NotBlank
    private String searchKey;
    @NotBlank
    private String searchValue;
}
