package com.example.paymentmanagement.service;

import com.example.paymentmanagement.accessor.NFTServiceAccessor;
import com.example.paymentmanagement.accessor.UserManagementAccessor;
import com.example.paymentmanagement.accessor.resource.NFT;
import com.example.paymentmanagement.accessor.resource.User;
import com.example.paymentmanagement.controller.dto.BuyDto;
import com.example.paymentmanagement.data.NFTTransaction;
import com.example.paymentmanagement.data.TransactionType;
import com.example.paymentmanagement.entity.Wallet;
import com.example.paymentmanagement.entity.WalletTransaction;
import com.example.paymentmanagement.exception.BadRequestException;
import com.example.paymentmanagement.exception.Error;
import com.example.paymentmanagement.repository.WalletRepository;
import com.example.paymentmanagement.repository.WalletTransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final WalletRepository walletRepository;
    private final WalletTransactionRepository walletTransactionRepository;
    private final UserManagementAccessor userManagementAccessor;
    private final NFTServiceAccessor nftServiceAccessor;

    @Transactional
    public WalletTransaction buy(BuyDto buyDto) {

        final User user = userManagementAccessor.getUserById(buyDto.getUserId());
        final NFT nft = nftServiceAccessor.getNftById(buyDto.getNftId());
        Optional<Wallet> wallet = walletRepository.findOptionalByUserIdAndCurrency(user.getId(), nft.getCurrency());

        if(wallet.isEmpty()) {
            throw new BadRequestException(Error.WALLET_DOESNT_EXIST.getErrorCode(), Error.WALLET_DOESNT_EXIST.getErrorMessage());
        }

        if(!nft.getIsOnSale()) {
            throw new BadRequestException(Error.NOT_ON_SALE.getErrorCode(), Error.NOT_ON_SALE.getErrorMessage());
        }

        if(wallet.get().getAmount() < nft.getPrice()) {
            throw new BadRequestException(Error.NOT_ENOUGH_BALANCE.getErrorCode(), Error.NOT_ENOUGH_BALANCE.getErrorMessage());
        }

        wallet.get().setAmount(wallet.get().getAmount() - nft.getPrice());
        walletRepository.save(wallet.get());
        log.info("Payment is made and decreased from users wallet with wallet id: {}", wallet.get().getId());

        nftServiceAccessor.addNFTTransaction(NFTTransaction.builder().nftId(nft.getId())
                .transactionType(TransactionType.SELL)
                .transactionDate(Instant.now())
                .build());

        log.info("NFT Transaction is sent to NFT Service");
        log.info("Saving wallet transaction in the repository...");

        return walletTransactionRepository.save(WalletTransaction.builder().walletId(wallet.get().getId())
                .amount(nft.getPrice())
                .date(Instant.now())
                .build());
    }
}
