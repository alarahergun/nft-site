package com.example.usermanagement.repository;

import com.example.usermanagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmailOrMsisdn(String email, String msisdn);
}
