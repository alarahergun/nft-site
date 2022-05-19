package com.example.paymentmanagement.controller;

import com.example.paymentmanagement.controller.dto.AddToWalletDto;
import com.example.paymentmanagement.data.frankfurter.FrankfurterCurrencyList;
import com.example.paymentmanagement.entity.Wallet;
import com.example.paymentmanagement.entity.WalletTransaction;
import com.example.paymentmanagement.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wallets")
public class WalletController {

    private final WalletService walletService;

    @GetMapping("/transactions")
    public ResponseEntity<List<WalletTransaction>> getAllWalletTransactions(){
        return ResponseEntity.ok(walletService.getAllWalletTransactions());
    }

    @GetMapping("/transactions/{transactionId}")
    public ResponseEntity<WalletTransaction> getWalletTransactionById(@RequestParam long transactionId) {
        return ResponseEntity.ok(walletService.getWalletTransactionById(transactionId));
    }

    @PostMapping("/add-to-wallet")
    public ResponseEntity<Wallet> addToWallet(@RequestBody AddToWalletDto addToWalletDto) {
        return ResponseEntity.ok(walletService.addCryptoMoneyToWallet(addToWalletDto));
    }

}
