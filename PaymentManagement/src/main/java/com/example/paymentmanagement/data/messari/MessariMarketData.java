package com.example.paymentmanagement.data.messari;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class MessariMarketData {

    @JsonProperty("price_btc")
    private double priceBtc;
    @JsonProperty("price_usd")
    private double priceUsd;
    @JsonProperty("price_eth")
    private double priceEth;
}
