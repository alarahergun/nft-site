package com.example.usermanagement.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;

@Getter
@Setter
public class UpdateUserInfoDto {

    @JsonProperty("userId")
    private long id;
    private String name;
    private String surname;
    @Email
    private String email;
    private String msisdn;
}
