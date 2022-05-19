package com.example.paymentmanagement.data.frankfurter;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
public class FrankfurterConversionRates {

    private int amount;
    private String base;
    private String date;
    private HashMap<String, Double>  rates;
}
