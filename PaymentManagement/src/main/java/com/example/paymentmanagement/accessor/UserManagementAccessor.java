package com.example.paymentmanagement.accessor;

import com.example.paymentmanagement.accessor.resource.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("NFT-USERMANAGEMENT")
public interface UserManagementAccessor {

    @GetMapping("/users/{userId}")
    User getUserById(@RequestParam long userId);

    @GetMapping("/users")
    List<User> getAllUsers();
}
