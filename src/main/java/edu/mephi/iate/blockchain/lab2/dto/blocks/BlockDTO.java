package edu.mephi.iate.blockchain.lab2.dto.blocks;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlockDTO {

    @JsonSetter("id")
    private long id;

    @JsonSetter("hash")
    private String hash;

    @JsonSetter("transaction_count")
    private long transactionCount;

    @JsonSetter("reward_usd")
    private double rewardUSD;
}
