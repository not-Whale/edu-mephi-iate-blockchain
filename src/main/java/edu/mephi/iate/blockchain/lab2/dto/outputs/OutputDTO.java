package edu.mephi.iate.blockchain.lab2.dto.outputs;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OutputDTO {

    @JsonSetter("block_id")
    private long blockID;

    @JsonSetter("transaction_id")
    private long transactionID;

    @JsonSetter("transaction_hash")
    private String transactionHash;

    @JsonSetter("recipient")
    private String recipient;

    @JsonSetter("fee")
    private long fee;
}
