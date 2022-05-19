package com.example.paymentmanagement.controller;

import com.example.paymentmanagement.controller.dto.BuyDto;
import com.example.paymentmanagement.entity.WalletTransaction;
import com.example.paymentmanagement.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/buy")
    public ResponseEntity<WalletTransaction> buy(@RequestBody BuyDto buyDto){
        return ResponseEntity.ok(paymentService.buy(buyDto));
    }
}