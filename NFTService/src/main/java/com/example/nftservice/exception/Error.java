package com.example.nftservice.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Error {

    NFT_NOT_FOUND("nftNotFound", "There isn't an NFT with this id."),
    NFT_TRANSACTION_NOT_FOUND("nftTransactionNotFound", "There isn't an NFT transaction with this id."),
    NFT_METADATA_NOT_FOUND("nftMetadataNotFound", "There isn't a metadata with this nftId."),
    NFT_NON_MUTABLE("nftNonMutable", "The content for this NFT has already been uploaded and it cannot be changed."),
    USER_DOESNT_EXIST("userDoesntExist","There isn't a user with those credentials."),
    NFT_ALREADY_MINTED("nftAlreadyMinted", "This NFT is already minted."),
    GOOGLE_DRIVE_ERROR("googleDriveError", "There is an error caused by Google Drive"),
    MISSING_CONTENT("missingContent", "There isn't an uploaded content, please upload your content.");

    private final String errorCode;
    private final String errorMessage;
    }
