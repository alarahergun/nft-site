package com.example.nftservice.accessor;

import com.example.nftservice.accessor.resource.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@FeignClient("NFT-USERMANAGEMENT")
public interface UserManagementAccessor {

    @PostMapping("/users/add-mint-to-user")
    void addMintToUser();

    @GetMapping("/users/{userId}")
    User getUserById(@PathVariable long userId);
}
