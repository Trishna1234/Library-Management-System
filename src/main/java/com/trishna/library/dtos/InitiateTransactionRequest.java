package com.trishna.library.dtos;

import com.trishna.library.models.TransactionType;
import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InitiateTransactionRequest {

    @NotNull
    private Integer studentId;

    @NotNull
    private Integer bookId;

//    @NotNull
//    private Integer adminId;

    @NotNull
    private TransactionType transactionType;

}
