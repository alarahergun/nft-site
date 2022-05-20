package com.example.nftservice.controller;

import com.example.nftservice.controller.dto.CreateNFTDto;
import com.example.nftservice.controller.resource.MintResource;
import com.example.nftservice.data.NFTMetadata;
import com.example.nftservice.entity.NFT;
import com.example.nftservice.entity.NFTTransaction;
import com.example.nftservice.service.NFTService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.File;
import java.util.List;

@RestController
@RequestMapping("/nft")
@RequiredArgsConstructor
public class NFTController {

    private final NFTService nftService;

    @GetMapping
    public ResponseEntity<List<NFT>> getAllNFTs() {
        return ResponseEntity.ok(nftService.getAllNFTs());
    }

    @GetMapping("/{nftId}")
    public ResponseEntity<NFT> getNFTById(@PathVariable long nftId) {
        return ResponseEntity.ok(nftService.getNFTById(nftId));
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<NFTTransaction>> getAllNFTTransactions() {
        return ResponseEntity.ok(nftService.getAllNFTTransactions());
    }

    @GetMapping("/transactions/{nftTransactionId}")
    public ResponseEntity<NFTTransaction> getTransaction(@PathVariable long nftTransactionId) {
        return ResponseEntity.ok(nftService.findNFTTransactionById(nftTransactionId));
    }

    @PostMapping("/post-sell-operations")
    public ResponseEntity<NFTTransaction> addNFTTransaction(@RequestBody NFTTransaction nftTransaction, @RequestParam long ownerId) {
        return ResponseEntity.ok(nftService.postSellOperations(nftTransaction, ownerId));
    }

    @PostMapping("/create")
    public ResponseEntity<NFT> createNFT(@RequestBody @Valid CreateNFTDto createNFTDto) {
        return ResponseEntity.ok(nftService.createNFT(createNFTDto));
    }

    @PostMapping("/mint")
    public ResponseEntity<MintResource> mintNFT(@RequestParam long nftId) {
        return ResponseEntity.ok(nftService.mintNFT(nftId));
    }

    @PostMapping("/upload-image")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void uploadImageToNFT(@RequestParam Long nftId, @RequestParam("file") File filepath) {
        nftService.uploadImageToNFT(nftId, filepath);
    }

    @PostMapping("/transfer")
    public ResponseEntity<NFTTransaction> transferNFT(@RequestParam long nftId, @RequestParam long userId){
        return ResponseEntity.ok(nftService.transferNFT(nftId, userId));
    }

    @GetMapping("/nftMetadata")
    public ResponseEntity<NFTMetadata> getNFTMetadata(@RequestParam long nftId) {
        return ResponseEntity.ok(nftService.getNFTMetadata(nftId));
    }
}
