package com.example.nftservice.entity;

import com.example.nftservice.data.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Entity
@Table(name = "NFT_Transaction")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NFTTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "nft_transaction_id")
    private long id;

    @Column(name = "nft_id")
    @NotNull
    private long nftId;

    @Column(name = "transaction_type")
    @NotNull
    private TransactionType transactionType;

    @Column(name = "transaction_date")
    @NotNull
    private Instant transactionDate;
}
