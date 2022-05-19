package com.example.nftservice.accessor.resource;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {

    private long id;
    private String name;
    private String surname;
    private String email;
    private String msisdn;
}
