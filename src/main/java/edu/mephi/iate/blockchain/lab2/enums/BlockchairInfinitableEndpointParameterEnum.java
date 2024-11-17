package edu.mephi.iate.blockchain.lab2.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BlockchairInfinitableEndpointParameterEnum {
    WHERE("q"),
    ORDER_BY("s"),
    LIMIT("limit"),
    OFFSET("offset"),
    GROUP_BY("a");

    private final String parameterName;
}
