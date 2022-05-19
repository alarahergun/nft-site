package com.example.paymentmanagement.accessor;

import com.example.paymentmanagement.accessor.resource.NFT;
import com.example.paymentmanagement.data.NFTTransaction;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("NFT-SERVICE")
public interface NFTServiceAccessor {

    @GetMapping("/nft/{nftId}")
    NFT getNftById(@RequestParam long nftId);

    @PostMapping("/nft/add-transaction")
    NFTTransaction addNFTTransaction(@RequestBody NFTTransaction nftTransaction);
}
