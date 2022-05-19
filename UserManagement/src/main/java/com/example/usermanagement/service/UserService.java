package com.example.usermanagement.service;

import com.example.usermanagement.controller.dto.CreateUserDto;
import com.example.usermanagement.controller.dto.UpdateUserInfoDto;
import com.example.usermanagement.entity.User;
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

    public User updateUserInfo(UpdateUserInfoDto updateUserInfoDto) {
        User userToUpdate = findUserByID(updateUserInfoDto.getId());
        userToUpdate.setEmail(updateUserInfoDto.getEmail());
        userToUpdate.setMsisdn(updateUserInfoDto.getMsisdn());
        userToUpdate.setName(updateUserInfoDto.getName());
        userToUpdate.setSurname(updateUserInfoDto.getSurname());

        log.info("User with id {} has been updated.", updateUserInfoDto.getId());
        return userToUpdate;
    }

    public User addNewUser(CreateUserDto dto) {
        User user = userRepository.findByEmailOrMsisdn(dto.getEmail(), dto.getMsisdn());
        if (user != null) {
            throw new BadRequestException(Error.USER_ALREADY_EXISTS.getErrorCode(),
                    Error.USER_ALREADY_EXISTS.getErrorMessage());
        }

        return userRepository.save(User.builder().name(dto.getName())
                .surname(dto.getSurname()).email(dto.getEmail())
                .msisdn(dto.getMsisdn()).build());
    }
}
