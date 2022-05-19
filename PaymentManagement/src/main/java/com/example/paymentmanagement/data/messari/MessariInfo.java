package com.example.paymentmanagement.data.messari;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class MessariInfo {

    @JsonProperty("data")
    private MessariData btcData;

}
