package com.example.paymentmanagement.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NFTTransaction {

    private long id;
    private long nftId;
    private TransactionType transactionType;
    private Instant transactionDate;

}
