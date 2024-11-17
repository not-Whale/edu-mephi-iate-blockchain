package edu.mephi.iate.blockchain.lab2.dto.transactions;

import com.fasterxml.jackson.annotation.JsonSetter;
import edu.mephi.iate.blockchain.lab2.dto.ContextDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionsDTO {

    @JsonSetter("data")
    private List<TransactionDTO> transactions;

    @JsonSetter("context")
    private ContextDTO context;
}
