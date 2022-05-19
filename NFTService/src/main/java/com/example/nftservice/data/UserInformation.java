package com.example.nftservice.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInformation {

    private long userId;
    private Boolean verified;
    private int share;
}
