package com.example.paymentmanagement.controller.dto;

import com.example.paymentmanagement.data.Currency;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddToWalletDto {

    private long userId;
    private String localCurrency;
    private Currency cryptoCurrency;
    private double localAmount;
    private double wantedAmount;
}
