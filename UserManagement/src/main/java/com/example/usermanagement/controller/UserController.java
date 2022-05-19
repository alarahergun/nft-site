package com.example.usermanagement.controller;

import com.example.usermanagement.controller.dto.CreateUserDto;
import com.example.usermanagement.controller.dto.UpdateUserInfoDto;
import com.example.usermanagement.entity.User;
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
    public ResponseEntity<User> getUserById(@PathVariable long userId) {
        return ResponseEntity.ok(userService.findUserByID(userId));
    }

    @PutMapping("/update-user-info")
    public ResponseEntity<User> updateUserInfo(@RequestBody @Valid UpdateUserInfoDto updateUserInfoDto) {
        return ResponseEntity.ok(userService.updateUserInfo(updateUserInfoDto));
    }

    @PostMapping("/new-user")
    public ResponseEntity<User> addNewUser(@RequestBody @Valid CreateUserDto createUserDto) {
        return ResponseEntity.ok(userService.addNewUser(createUserDto));
    }
}
