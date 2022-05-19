package com.example.nftservice.controller.dto;

import com.example.nftservice.data.Attribute;
import com.example.nftservice.data.Collection;
import com.example.nftservice.data.UserInformation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class CreateNFTDto {

    @NotNull(message = "Currency should be specified!")
    private final Currency currency;
    @NotNull(message = "isMutable cannot be empty")
    private final Boolean isMutable;
    @NotNull(message = "Price cannot be empty!")
    private final double price;
    @NotBlank(message = "Name field cannot be empty!")
    private final String name;
    @NotBlank(message = "Symbol field cannot be empty!")
    private final String symbol;
    @NotBlank(message = "Description field cannot be empty!")
    private final String description;
    @URL(message = "External URL should be in valid format!")
    private final String externalUrl;
    @NotNull
    private final long userId;
    @NotEmpty
    private final List<UserInformation> creators;
    private final List<Collection> collections;
    private final List<Attribute> attributes;
    private final List<String> uses;
}
