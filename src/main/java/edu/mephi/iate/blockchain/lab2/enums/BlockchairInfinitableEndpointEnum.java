package edu.mephi.iate.blockchain.lab2.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BlockchairInfinitableEndpointEnum {
    OUTPUTS("outputs"),
    TRANSACTIONS("transactions"),
    BLOCKS("blocks");

    private final String endpointName;
}
