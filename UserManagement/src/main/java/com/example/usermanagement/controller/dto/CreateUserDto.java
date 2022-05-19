package com.example.usermanagement.controller.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CreateUserDto {

    @NotBlank
    private String name;
    @NotBlank
    private String surname;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String msisdn;
}
