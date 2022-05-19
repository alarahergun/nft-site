package com.example.usermanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    @NotBlank(message = "name cannot be empty!")
    private String name;

    @Column(name = "surname")
    @NotBlank(message = "surname cannot be empty!")
    private String surname;

    @Column(name = "email")
    @Email
    @NotBlank(message = "email cannot be empty!")
    private String email;

    @Column(name = "msisdn")
    @Size(min = 10, max = 10, message = "Please enter your number without 0!")
    @NotBlank(message = "msisdn cannot be empty!")
    private String msisdn;
}
