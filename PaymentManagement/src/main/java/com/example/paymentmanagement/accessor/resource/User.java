package com.example.paymentmanagement.accessor.resource;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class User {

    private final long id;
    private final String name;
    private final String surname;
    private final String email;
    private final String msisdn;
}
