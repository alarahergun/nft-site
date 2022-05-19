package com.example.nftservice.entity;

import com.example.nftservice.controller.dto.Currency;
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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "NFT")
@Builder
public class NFT {

    @Id
    @Column(name = "nft_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "currency")
    @NotNull
    private Currency currency;

    @Column(name = "price")
    @NotNull
    private double price;

    @Column(name = "primary_sale_happened")
    @NotNull
    private Boolean primarySaleHappened = false;

    @Column(name = "is_mutable")
    @NotNull
    private Boolean isMutable = false;

    @Column(name = "is_on_sale")
    @NotNull
    private Boolean isOnSale = false;
}
