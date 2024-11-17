package edu.mephi.iate.blockchain.lab2.common.mappers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.mephi.iate.blockchain.lab2.dto.transactions.TransactionDTO;
import edu.mephi.iate.blockchain.lab2.dto.transactions.TransactionsDTO;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@Slf4j
@UtilityClass
public class TransactionsMapper {

    private static final ObjectMapper objectMapper = new ObjectMapper()
            .findAndRegisterModules()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public static Optional<TransactionsDTO> mapToTransactionsDTO(String response) {
        try {
            return Optional.of(objectMapper.readValue(response, TransactionsDTO.class));
        } catch (JsonProcessingException e) {
            log.error("Error during mapping TransactionsDTO. Message: {}", e.getMessage());
            return Optional.empty();
        }
    }

    public static Optional<TransactionDTO> mapToTransactionDTO(String response) {
        try {
            TransactionsDTO transactionsDTO = objectMapper.readValue(response, TransactionsDTO.class);
            List<TransactionDTO> transactionDTOList = transactionsDTO.getTransactions();
            if (!transactionDTOList.isEmpty()) {
                return Optional.of(transactionDTOList.getFirst());
            } else {
                return Optional.empty();
            }
        } catch (JsonProcessingException e) {
            log.error("Error during mapping TransactionDTO. Message: {}", e.getMessage());
            return Optional.empty();
        }
    }
}
