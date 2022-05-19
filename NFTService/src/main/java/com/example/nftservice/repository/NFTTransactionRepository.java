package com.example.nftservice.repository;

import com.example.nftservice.entity.NFTTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NFTTransactionRepository extends JpaRepository<NFTTransaction, Long> {

}
