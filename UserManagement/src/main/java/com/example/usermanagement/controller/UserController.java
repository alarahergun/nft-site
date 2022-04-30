package com.example.usermanagement.controller;

import com.example.usermanagement.domain.User;
import com.example.usermanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserByUserId(@PathVariable long userId) {
        return ResponseEntity.ok(userService.findUserByID(userId));
    }

    @PutMapping("/updateUserInfo")
    public ResponseEntity<User> updateUserInfo(@RequestBody @Valid User user) {
        return ResponseEntity.ok(userService.updateUserInfo(user));
    }

    @PostMapping("/mint-nft")
    public void mintNFT() {
        return;
    }




}
