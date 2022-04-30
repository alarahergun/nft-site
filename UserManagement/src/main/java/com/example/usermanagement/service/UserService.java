package com.example.usermanagement.service;

import com.example.usermanagement.domain.NFT;
import com.example.usermanagement.domain.User;
import com.example.usermanagement.exception.BadRequestException;
import com.example.usermanagement.exception.Error;
import com.example.usermanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User findUserByID(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException(Error.USER_NOT_FOUND.getErrorCode(),
                        Error.USER_NOT_FOUND.getErrorMessage()));
    }

    public User updateUserInfo(User user) {
        User userToUpdate = findUserByID(user.getId());
        userToUpdate.setEmail(user.getEmail());
        userToUpdate.setMsisdn(user.getMsisdn());
        userToUpdate.setName(user.getName());
        userToUpdate.setSurname(user.getSurname());

        log.info("User with id {} has been updated.", user.getId());
        return user;
    }
}
