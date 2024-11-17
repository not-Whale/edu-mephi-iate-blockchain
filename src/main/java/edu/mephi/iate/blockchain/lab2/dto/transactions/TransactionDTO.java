package edu.mephi.iate.blockchain.lab2.dto.transactions;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {

    @JsonSetter("block_id")
    private long blockID;

    @JsonSetter("id")
    private long id;

    @JsonSetter("hash")
    private String hash;

    @JsonSetter("date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @JsonSetter("time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time;

    @JsonSetter("input_total_usd")
    private long inputTotalUSD;

    @JsonSetter("fee_usd")
    private long feeUSD;
}
