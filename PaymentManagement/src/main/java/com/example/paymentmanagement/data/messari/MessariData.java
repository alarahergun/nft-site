package com.example.paymentmanagement.data.messari;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class MessariData {

    @JsonProperty("market_data")
    private MessariMarketData messariMarketData;

}
