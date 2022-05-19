package com.example.nftservice.controller.resource;

import com.example.nftservice.entity.NFT;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MintResource {

    private NFT nft;
    private String token;
}
