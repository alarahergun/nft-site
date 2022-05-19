package com.example.nftservice.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class NFTMetadata {
    private String name;
    private String symbol;
    private String imageUrl;
    private String description;
    private String externalUrl;
    private long ownerId;
    private List<UserInformation> creators;
    private List<Collection> collections;
    private List<Attribute> attributes;
    private List<String> uses;
    private String token = null;
}
