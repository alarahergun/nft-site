package com.example.nftservice.service;

import com.example.nftservice.accessor.UserManagementAccessor;
import com.example.nftservice.accessor.resource.User;
import com.example.nftservice.config.GoogleDriveConfig;
import com.example.nftservice.controller.dto.CreateNFTDto;
import com.example.nftservice.controller.resource.MintResource;
import com.example.nftservice.data.NFTMetadata;
import com.example.nftservice.data.TransactionType;
import com.example.nftservice.entity.NFT;
import com.example.nftservice.entity.NFTTransaction;
import com.example.nftservice.exception.BadRequestException;
import com.example.nftservice.exception.Error;
import com.example.nftservice.repository.NFTRepository;
import com.example.nftservice.repository.NFTTransactionRepository;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.model.File;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class NFTService {

    private final NFTRepository nftRepository;
    private final NFTTransactionRepository nftTransactionRepository;
    private final UserManagementAccessor userManagementAccessor;
    private final RedisTemplate<String, String> redisTemplate;
    private final RedisTemplate<String, NFTMetadata> redisTemplateForMetadata;

    @Value("${google.drive.folder.id}")
    private String folderId;

    @Transactional
    public NFT createNFT(CreateNFTDto createNFTDto) {
        try {
            createNFTDto.getCreators().forEach(creator -> userManagementAccessor.getUserById(creator.getUserId()));
            userManagementAccessor.getUserById(createNFTDto.getUserId());
        } catch (Exception e) {
            throw new BadRequestException(Error.USER_DOESNT_EXIST.getErrorCode(),
                    Error.USER_DOESNT_EXIST.getErrorMessage());
        }

        final NFTMetadata nftMetadata =
                NFTMetadata.builder()
                        .name(createNFTDto.getName())
                        .symbol(createNFTDto.getSymbol())
                        .description(createNFTDto.getDescription())
                        .externalUrl(
                                ObjectUtils.isNotEmpty(createNFTDto.getExternalUrl()) ? createNFTDto.getExternalUrl()
                                        : null)
                        .ownerId(createNFTDto.getUserId())
                        .creators(createNFTDto.getCreators())
                        .collections(
                                ObjectUtils.isNotEmpty(createNFTDto.getCollections()) ? createNFTDto.getCollections()
                                        : null)
                        .attributes(ObjectUtils.isNotEmpty(createNFTDto.getAttributes()) ? createNFTDto.getAttributes()
                                : null)
                        .uses(ObjectUtils.isNotEmpty(createNFTDto.getUses()) ? createNFTDto.getUses() : null)
                        .build();

        final NFT nft = nftRepository.save(NFT.builder().currency(createNFTDto.getCurrency())
                .isMutable(createNFTDto.getIsMutable())
                .price(createNFTDto.getPrice())
                .isOnSale(false)
                .primarySaleHappened(false)
                .build());

        saveNFTMetadata(nft.getId(), nftMetadata);
        return nft;
    }

    @Transactional
    public MintResource mintNFT(long nftId) {
        final Optional<NFT> nft = nftRepository.findById(nftId);
        if (nft.isEmpty()) {
            throw new BadRequestException(Error.NFT_NOT_FOUND.getErrorCode(), Error.NFT_NOT_FOUND.getErrorMessage());
        }

        if (nft.get().getPrimarySaleHappened() || getNFTMetadata(nftId).getToken() != null) {
            throw new BadRequestException(Error.NFT_ALREADY_MINTED.getErrorCode(),
                    Error.NFT_ALREADY_MINTED.getErrorMessage());
        }

        if (Boolean.FALSE.equals(redisTemplate.opsForHash().hasKey("nft-upload", String.valueOf(nftId))) ||
                ObjectUtils.isEmpty(getNFTMetadata(nftId).getImageUrl())) {
            throw new BadRequestException(Error.MISSING_CONTENT.getErrorCode(),
                    Error.MISSING_CONTENT.getErrorMessage());
        }

        nft.get().setIsOnSale(true);

        //nft transaction is created and saved
        nftTransactionRepository.save(NFTTransaction.builder()
                .nftId(nftId)
                .transactionType(TransactionType.MINT)
                .transactionDate(Instant.now())
                .build());

        // examplary token is created and assigned
        NFTMetadata nftMetadata = getNFTMetadata(nftId);
        String token = UUID.randomUUID().toString().replace("-", "");
        nftMetadata.setToken(token);
        saveNFTMetadata(nftId, nftMetadata);

        return MintResource.builder()
                .nft(nft.get())
                .token(token).build();
    }

    @SneakyThrows
    public void uploadImageToNFT(long nftId, java.io.File filepath) {
        final NFT nft = getNFTById(nftId);

        if (Boolean.TRUE.equals(redisTemplate.opsForHash().hasKey("nft-upload", String.valueOf(nftId)))
                && Boolean.FALSE.equals(
                nft.getIsMutable())) {
            throw new BadRequestException(Error.NFT_NON_MUTABLE.getErrorCode(),
                    Error.NFT_NON_MUTABLE.getErrorMessage());
        }

        try {
            //uploading file to google drive
            File fileMetadata = new File();
            fileMetadata.setName(nftId + ".jpg");
            fileMetadata.setParents(Collections.singletonList(folderId));
            FileContent mediaContent = new FileContent("image/jpeg", filepath);
            File file = GoogleDriveConfig.getGoogleDrive().files().create(fileMetadata, mediaContent)
                    .setFields("id, parents, webViewLink")
                    .execute();
            log.info("Upload successful for NFT with id {}. ", nftId);
            log.info("Uploaded image's Google Drive file ID: {}", file.getId());

            //uploading image info to redis in nftId-imageId format
            redisTemplate.opsForHash().put("nft-upload", String.valueOf(nftId), file.getId());

            //updating imageUrl in metadata
            NFTMetadata nftMetadata = getNFTMetadata(nftId);
            nftMetadata.setImageUrl(file.getWebViewLink());
            saveNFTMetadata(nftId, nftMetadata);
        } catch (Exception e) {
            throw new BadRequestException(Error.GOOGLE_DRIVE_ERROR.getErrorCode(),
                    Error.GOOGLE_DRIVE_ERROR.getErrorMessage());
        }
    }

    public NFTTransaction transferNFT(long nftId, long userId) {
        final NFT nft = getNFTById(nftId);
        final User user = userManagementAccessor.getUserById(userId);

        //updating owner info
        NFTMetadata nftMetadata = getNFTMetadata(nftId);
        nftMetadata.setOwnerId(user.getId());
        saveNFTMetadata(nft.getId(), nftMetadata);

        return nftTransactionRepository.save(NFTTransaction.builder()
                .nftId(nftId)
                .transactionType(TransactionType.TRANSFER)
                .transactionDate(Instant.now())
                .build());
    }

    public List<NFT> getAllNFTs() {
        return nftRepository.findAll();
    }

    public NFT getNFTById(long nftId) {
        Optional<NFT> nft = nftRepository.findById(nftId);
        if (nft.isEmpty()) {
            throw new BadRequestException(Error.NFT_NOT_FOUND.getErrorCode(), Error.NFT_NOT_FOUND.getErrorMessage());
        }

        return nft.get();
    }

    public List<NFTTransaction> getAllNFTTransactions() {
        return nftTransactionRepository.findAll();
    }

    public NFTTransaction findNFTTransactionById(long nftTransactionId) {
        Optional<NFTTransaction> nftTransaction = nftTransactionRepository.findById(nftTransactionId);
        if (nftTransaction.isEmpty()) {
            throw new BadRequestException(Error.NFT_TRANSACTION_NOT_FOUND.getErrorCode(), Error.NFT_TRANSACTION_NOT_FOUND.getErrorMessage());
        }

        return nftTransaction.get();
    }

    public NFTTransaction postSellOperations(NFTTransaction nftTransaction, long ownerId) {

        NFTMetadata nftMetadata = getNFTMetadata(nftTransaction.getNftId());
        nftMetadata.setOwnerId(ownerId);
        return nftTransactionRepository.save(nftTransaction);
    }

    public NFTMetadata getNFTMetadata(long nftId) {
        NFTMetadata nftMetadata = redisTemplateForMetadata.opsForValue().get(String.valueOf(nftId));

        if(nftMetadata == null) {
            throw new BadRequestException(Error.NFT_METADATA_NOT_FOUND.getErrorCode(), Error.NFT_METADATA_NOT_FOUND.getErrorMessage());
        }

        return nftMetadata;
    }

    public void saveNFTMetadata(long nftId, NFTMetadata nftMetadata) {
        redisTemplateForMetadata.opsForValue().set(String.valueOf(nftId), nftMetadata);
    }
}
