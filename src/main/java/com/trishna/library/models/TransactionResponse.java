package com.trishna.library.models;

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
