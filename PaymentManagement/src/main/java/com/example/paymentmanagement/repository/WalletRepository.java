package com.example.paymentmanagement.repository;

import com.example.paymentmanagement.data.Currency;
import com.example.paymentmanagement.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {

    Optional<Wallet> findOptionalByUserIdAndCurrency(long userId, Currency currency);
}
