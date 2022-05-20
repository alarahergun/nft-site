package com.example.paymentmanagement.service;

import com.example.paymentmanagement.accessor.UserManagementAccessor;
import com.example.paymentmanagement.accessor.resource.NFT;
import com.example.paymentmanagement.accessor.resource.User;
import com.example.paymentmanagement.accessor.resource.UserInformation;
import com.example.paymentmanagement.controller.dto.AddToWalletDto;
import com.example.paymentmanagement.data.Currency;
import com.example.paymentmanagement.data.frankfurter.FrankfurterConversionRates;
import com.example.paymentmanagement.data.frankfurter.FrankfurterCurrencyList;
import com.example.paymentmanagement.data.messari.MessariInfo;
import com.example.paymentmanagement.entity.Wallet;
import com.example.paymentmanagement.entity.WalletTransaction;
import com.example.paymentmanagement.exception.BadRequestException;
import com.example.paymentmanagement.exception.Error;
import com.example.paymentmanagement.repository.WalletRepository;
import com.example.paymentmanagement.repository.WalletTransactionRepository;
import com.example.paymentmanagement.util.Constants;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import java.net.URI;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class WalletService {

    private final WalletRepository walletRepository;
    private final WalletTransactionRepository walletTransactionRepository;
    private final UserManagementAccessor userManagementAccessor;

    private RestTemplate restTemplate = new RestTemplate();

    @Value("${frankfurter.url}")
    private String frankfurterUrl;
    @Value("${messari.url.btc}")
    private String messariApiBtcUrl;
    @Value("${messari.url.eth}")
    private String messariApiEthUrl;

    public List<WalletTransaction> getAllWalletTransactions() {
        return walletTransactionRepository.findAll();
    }

    public WalletTransaction getWalletTransactionById(long transactionId) {
        Optional<WalletTransaction> walletTransaction = walletTransactionRepository.findById(transactionId);

        if (walletTransaction.isEmpty()) {
            throw new BadRequestException(Error.WALLET_TRANSACTION_DOESNT_EXIST.getErrorCode(),
                    Error.WALLET_TRANSACTION_DOESNT_EXIST.getErrorMessage());
        }

        return walletTransaction.get();
    }

    @Transactional
    public Wallet createOrReturnWallet(User user, Currency currency) {
        Optional<Wallet> walletByCurrency = walletRepository.findOptionalByUserIdAndCurrency(user.getId(), currency);

        if (walletByCurrency.isPresent()) {
            return walletByCurrency.get();
        }

        log.info("There isn't a wallet registered for user with id {} in {} crytocurrency. Creating new one...",
                user.getId(), currency.getCurrency());
        return walletRepository.save(Wallet.builder()
                .userId(user.getId())
                .currency(currency)
                .amount(0)
                .build());
    }

    @SneakyThrows
    public FrankfurterCurrencyList getCurrencies() {
        ParameterizedTypeReference<HashMap<String, String>> responseType = new ParameterizedTypeReference<>() {
        };
        HashMap<String, String> currencies = restTemplate.exchange(new URI(frankfurterUrl + "/currencies"),
                HttpMethod.GET, null,
                responseType).getBody();

        FrankfurterCurrencyList frankfurterCurrencyList = new FrankfurterCurrencyList();
        frankfurterCurrencyList.setCurrencies(currencies);

        return frankfurterCurrencyList;
    }

    public FrankfurterConversionRates getConversionRates() {
        String urlTemplate = UriComponentsBuilder.fromHttpUrl(frankfurterUrl + "/latest").queryParam("from", "{from}")
                .encode().toUriString();

        Map<String, String> params = new HashMap<>();
        params.put("from", Constants.US_DOLLAR);

        return restTemplate.exchange(urlTemplate, HttpMethod.GET, null, FrankfurterConversionRates.class, params)
                .getBody();
    }

    @Transactional
    public Wallet addCryptoMoneyToWallet(AddToWalletDto addToWalletDto) {
        List<User> users = userManagementAccessor.getAllUsers();

        Optional<User> userToUpdate = users.stream()
                .filter(user -> user.getId() == addToWalletDto.getUserId()).findAny();

        if (userToUpdate.isEmpty()) {
            throw new BadRequestException(Error.USER_DOESNT_EXIST.getErrorCode(),
                    Error.USER_DOESNT_EXIST.getErrorMessage());
        }

        final FrankfurterCurrencyList frankfurterCurrencyList = getCurrencies();

        //control currencies
        if (!frankfurterCurrencyList.getCurrencies().containsKey(addToWalletDto.getLocalCurrency())) {
            throw new BadRequestException(Error.CURRENCY_DOESNT_EXIST.getErrorCode(),
                    Error.CURRENCY_DOESNT_EXIST.getErrorMessage());
        }

        final double amount = convertToLocalCurrency(addToWalletDto.getCryptoCurrency(),
                addToWalletDto.getWantedAmount(), addToWalletDto.getLocalCurrency());
        if (amount > addToWalletDto.getLocalAmount()) {
            throw new BadRequestException(Error.NOT_ENOUGH_BALANCE.getErrorCode(),
                    Error.NOT_ENOUGH_BALANCE.getErrorMessage());
        }

        Wallet wallet = createOrReturnWallet(userToUpdate.get(), addToWalletDto.getCryptoCurrency());
        wallet.setAmount(wallet.getAmount() + addToWalletDto.getWantedAmount());

        walletTransactionRepository.save(
                WalletTransaction.builder()
                        .walletId(wallet.getId())
                        .amount(wallet.getAmount())
                        .date(Instant.now())
                        .build());

        return wallet;
    }

    @SneakyThrows
    public MessariInfo getMessariInfo(boolean isBtc) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");

        try {
            if (isBtc) {
                return restTemplate.exchange(new URI(messariApiBtcUrl), HttpMethod.GET, null, MessariInfo.class)
                        .getBody();
            }
            return restTemplate.exchange(new URI(messariApiEthUrl), HttpMethod.GET, null, MessariInfo.class).getBody();
        } catch (Exception e) {
            throw new BadRequestException(Error.MESSARI_API_ERROR.getErrorCode(),
                    Error.MESSARI_API_ERROR.getErrorMessage());
        }
    }

    public double convertToLocalCurrency(Currency cyptoCurrency, double wantedAmount, String localCurrency) {
        MessariInfo messariInfo;

        if (cyptoCurrency.equals(Currency.ETH)) {
            messariInfo = getMessariInfo(false);
        } else {
            messariInfo = getMessariInfo(true);
        }

        FrankfurterConversionRates frankfurterConversionRates = getConversionRates();
        final double localToUsdRate = frankfurterConversionRates.getRates().get(localCurrency);

        return wantedAmount * messariInfo.getBtcData().getMessariMarketData().getPriceUsd() * localToUsdRate;
    }

    @Transactional
    public void addNFTValueToWallets(List<UserInformation> userInformations, NFT nft) {

        log.info("Sold is made for NFT with id: {} Adding value to creators...", nft.getId());

        for (UserInformation userInformation : userInformations) {
            Wallet wallet = createOrReturnWallet(userManagementAccessor.getUserById(userInformation.getUserId()),
                    nft.getCurrency());
            wallet.setAmount(wallet.getAmount() + nft.getPrice() * (double) userInformation.getShare() / 100);
            log.info("Payment is made to creator with id {}, share: {}, currency: {}, amount: {}",
                    userInformation.getUserId(), (double) userInformation.getShare() / 100,
                    nft.getCurrency().getCurrency(), nft.getPrice() * (double) userInformation.getShare() / 100);

            walletRepository.save(wallet);

            walletTransactionRepository.save(WalletTransaction.builder().walletId(wallet.getId())
                    .amount(nft.getPrice() * (double) userInformation.getShare() / 100)
                    .date(Instant.now())
                    .build());
        }

    }
}
