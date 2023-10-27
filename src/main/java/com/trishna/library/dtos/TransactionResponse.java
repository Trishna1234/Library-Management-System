package com.trishna.library.dtos;

import com.trishna.library.models.utils.TransactionStatus;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TransactionResponse {
    private String transactionId;
    private TransactionStatus transactionStatus;
}
