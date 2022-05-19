package com.example.nftservice.repository;

import com.example.nftservice.entity.NFT;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NFTRepository extends JpaRepository<NFT, Long> {
}
