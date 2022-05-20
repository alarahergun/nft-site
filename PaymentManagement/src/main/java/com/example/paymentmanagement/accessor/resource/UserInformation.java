package com.example.paymentmanagement.accessor.resource;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInformation {

    private long userId;
    private Boolean verified;
    private int share;
}
