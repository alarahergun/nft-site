package com.example.paymentmanagement.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Currency {
    BTC("Bitcoin"),
    ETH("Ethereum");
    private final String currency;
}
