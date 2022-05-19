package com.example.paymentmanagement.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Error {

    NOT_ENOUGH_BALANCE("notEnoughBalance", "You don't have enough balance."),
    CURRENCY_DOESNT_EXIST("currencyDoesntExist", "You can't work with this currency, it is not supported."),
    NOT_ON_SALE("notOnSale", "This NFT is not on sale, please try another one."),
    WALLET_TRANSACTION_DOESNT_EXIST("walletTransactionDoesntExist", "Wallet transaction doesn't exist in the repository."),
    WALLET_DOESNT_EXIST("walletDoesntExist", "This user doesn't have a wallet with the currency of this payment. Please create one."),
    MESSARI_API_ERROR("messariApiError", "There is an error with Messari Api, please try later."),
    USER_DOESNT_EXIST("userDoesntExist", "There isn't a user with those credentials.");

    private final String errorCode;
    private final String errorMessage;
}
