package com.trishna.library.dtos;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateRequest {

    @NotBlank
    private String updateKey;
    @NotBlank
    private String updateValue;
}
