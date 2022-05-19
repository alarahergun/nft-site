package com.example.paymentmanagement.accessor.resource;

import com.example.paymentmanagement.data.Currency;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NFT {

    private final long id;
    private final Currency currency;
    private final double price;
    private final Boolean primarySaleHappened;
    private final Boolean isMutable;
    private final Boolean isOnSale;
}
