package com.example.paymentmanagement.accessor.resource;

import lombok.Getter;
import java.util.List;

@Getter
public class NFTMetadata {
    private long ownerId;
    private List<UserInformation> creators;
}
