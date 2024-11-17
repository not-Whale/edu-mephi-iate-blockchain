package edu.mephi.iate.blockchain.lab2.common;

import edu.mephi.iate.blockchain.lab2.common.mappers.BlocksMapper;
import edu.mephi.iate.blockchain.lab2.common.mappers.CountMapper;
import edu.mephi.iate.blockchain.lab2.common.mappers.OutputsMapper;
import edu.mephi.iate.blockchain.lab2.common.mappers.TransactionsMapper;
import edu.mephi.iate.blockchain.lab2.dto.blocks.BlockDTO;
import edu.mephi.iate.blockchain.lab2.dto.blocks.BlocksDTO;
import edu.mephi.iate.blockchain.lab2.dto.counts.CountDTO;
import edu.mephi.iate.blockchain.lab2.dto.outputs.OutputDTO;
import edu.mephi.iate.blockchain.lab2.dto.outputs.OutputsDTO;
import edu.mephi.iate.blockchain.lab2.dto.transactions.TransactionDTO;
import edu.mephi.iate.blockchain.lab2.dto.transactions.TransactionsDTO;
import edu.mephi.iate.blockchain.lab2.enums.BlockchairInfinitableEndpointEnum;
import edu.mephi.iate.blockchain.lab2.enums.BlockchairInfinitableEndpointParameterEnum;
import edu.mephi.iate.blockchain.lab2.http_client.BitcoinStatsBlockchairAPIHttpClient;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@UtilityClass
public class BlockchairAPIUtils {

    private static final BitcoinStatsBlockchairAPIHttpClient httpClient
            = new BitcoinStatsBlockchairAPIHttpClient("https://api.blockchair.com/bitcoin/");

    /**
     * Вопрос 1. Генезис-блок Bitcoin
     */
    public static Optional<BlockDTO> getBitcoinGenesisBlock() {
        Optional<String> response = httpClient.get(
                BlockchairInfinitableEndpointEnum.BLOCKS,
                Map.of(BlockchairInfinitableEndpointParameterEnum.WHERE, List.of("id(0)"))
        );
        return response.isPresent() ? BlocksMapper.mapToBlockDTO(response.get()) : Optional.empty();
    }

    /**
     * Вопрос 2. Первая транзакция в первом блоке 11.10.2022 по utc + 0
     */
    public static Optional<TransactionDTO> getFirstTransactionFromFirstBlock2022_10_11() {
        Optional<String> response = httpClient.get(
                BlockchairInfinitableEndpointEnum.TRANSACTIONS,
                Map.of(BlockchairInfinitableEndpointParameterEnum.WHERE, List.of("time(2022-10-11)"),
                        BlockchairInfinitableEndpointParameterEnum.ORDER_BY, List.of("block_id(asc)", "id(asc)"),
                        BlockchairInfinitableEndpointParameterEnum.LIMIT, List.of("1"))
        );
        return response.isPresent() ? TransactionsMapper.mapToTransactionDTO(response.get()) : Optional.empty();
    }

    /**
     * Вопрос 3: Найти адреса, на которые произведена была только одна транзакция, в первых 10 блоках за 01.10.2022.
     */
    public static List<String> getAddressesWithOnly1TransactionFromTop10Blocks2022_10_01() {
        Optional<String> responseBlocks = httpClient.get(
                BlockchairInfinitableEndpointEnum.BLOCKS,
                Map.of(BlockchairInfinitableEndpointParameterEnum.WHERE, List.of("time(2022-10-01)"),
                        BlockchairInfinitableEndpointParameterEnum.ORDER_BY, List.of("id(asc)"),
                        BlockchairInfinitableEndpointParameterEnum.LIMIT, List.of("10"))
        );
        if (responseBlocks.isEmpty()) {
            return List.of();
        }

        Optional<BlocksDTO> blocksDTOOpt = BlocksMapper.mapToBlocksDTO(responseBlocks.get());
        if (blocksDTOOpt.isEmpty()) {
            return List.of();
        }

        List<String> result = new ArrayList<>();
        List<Long> blockIds = blocksDTOOpt.get().getBlocks().stream().map(BlockDTO::getId).toList();
        for (long id : blockIds) {
            try {
                Thread.sleep(5_000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            Optional<String> response = httpClient.get(
                    BlockchairInfinitableEndpointEnum.OUTPUTS,
                    Map.of(BlockchairInfinitableEndpointParameterEnum.WHERE, List.of("block_id(" + id + ")"))
            );
            if (response.isEmpty()) {
                continue;
            }

            Optional<OutputsDTO> outputsDTOOpt = OutputsMapper.mapToOutputsDTO(response.get());
            if (outputsDTOOpt.isEmpty()) {
                continue;
            }

            Map<String, Integer> outputsMap = outputsDTOOpt.get().getOutputs().stream()
                    .collect(Collectors.toMap(
                            OutputDTO::getRecipient,
                            outputDTO -> 1,
                            Integer::sum));

            result.addAll(outputsMap.entrySet().stream()
                    .filter(entry -> entry.getValue() == 1)
                    .map(Map.Entry::getKey)
                    .toList());
        }
        return result;
    }

    /**
     * Вопрос 4. Хеш первых 3 блоков за 08.10.2022.
     */
    public static List<String> getTop3BlockHashes2022_10_08() {
        Optional<String> response = httpClient.get(
                BlockchairInfinitableEndpointEnum.BLOCKS,
                Map.of(BlockchairInfinitableEndpointParameterEnum.WHERE, List.of("time(2022-10-08)"),
                        BlockchairInfinitableEndpointParameterEnum.ORDER_BY, List.of("id(asc)"),
                        BlockchairInfinitableEndpointParameterEnum.LIMIT, List.of("3"))
        );
        return response.map(responseBlocks -> TransactionsMapper.mapToTransactionsDTO(responseBlocks)
                .map(transactionsDTO -> transactionsDTO.getTransactions().stream()
                        .map(TransactionDTO::getHash)
                        .toList())
                .orElseGet(List::of)).orElseGet(List::of);
    }

    /**
     * Вопрос 5. Подсчитать общее количество блоков за 01.10.2022.
     */
    public static Long countAllBlocks2022_10_01() {
        Optional<String> response = httpClient.get(
                BlockchairInfinitableEndpointEnum.BLOCKS,
                Map.of(BlockchairInfinitableEndpointParameterEnum.WHERE, List.of("time(2022-10-01)"),
                        BlockchairInfinitableEndpointParameterEnum.GROUP_BY, List.of("count()"))
        );
        return response.map(responseBlocks -> CountMapper.mapToCountDTO(responseBlocks)
                        .map(CountDTO::getCount)
                        .orElse(0L))
                .orElse(0L);
    }

    /**
     * Вопрос 6. Найти 10 самых больших транзакций за 02.10.2022 по передаваемой сумме валюты.
     */
    public static List<TransactionDTO> getTop10TransactionsWithMaxInputTotalUsd2022_10_02() {
        Optional<String> response = httpClient.get(
                BlockchairInfinitableEndpointEnum.TRANSACTIONS,
                Map.of(BlockchairInfinitableEndpointParameterEnum.WHERE, List.of("time(2022-10-02)"),
                        BlockchairInfinitableEndpointParameterEnum.ORDER_BY, List.of("input_total_usd(desc)"),
                        BlockchairInfinitableEndpointParameterEnum.LIMIT, List.of("10"))
        );
        return response.map(responseTransactions -> TransactionsMapper.mapToTransactionsDTO(responseTransactions)
                        .map(TransactionsDTO::getTransactions)
                        .orElseGet(List::of))
                .orElseGet(List::of);
    }

    /**
     * Вопрос 7: Адрес майнер, кто получил наибольшее количество комиссии за 02.10.2022.
     */
    public static Optional<String> getAddressWithMaxFee2022_10_02() {
        Optional<String> responseTransaction = httpClient.get(
                BlockchairInfinitableEndpointEnum.TRANSACTIONS,
                Map.of(BlockchairInfinitableEndpointParameterEnum.WHERE, List.of("time(2022-10-02)"),
                        BlockchairInfinitableEndpointParameterEnum.ORDER_BY, List.of("fee(desc)"),
                        BlockchairInfinitableEndpointParameterEnum.LIMIT, List.of("1"))
        );
        if (responseTransaction.isEmpty()) {
            return Optional.empty();
        }

        Optional<TransactionDTO> transactionDTOOpt = TransactionsMapper.mapToTransactionDTO(responseTransaction.get());
        if (transactionDTOOpt.isEmpty()) {
            return Optional.empty();
        }

        Optional<String> response = httpClient.get(
                BlockchairInfinitableEndpointEnum.OUTPUTS,
                Map.of(BlockchairInfinitableEndpointParameterEnum.WHERE, List.of("transaction_id(" + transactionDTOOpt.get().getId() + ")"))
        );
        if (response.isEmpty()) {
            return Optional.empty();
        }
        Optional<OutputDTO> outputDTOOpt = OutputsMapper.mapToOutputDTO(response.get());
        return outputDTOOpt.map(OutputDTO::getRecipient);
    }

    /**
     * Вопрос 8: Блок с наибольшим количеством транзакций за 01.10.2022.
     */
    public static Optional<BlockDTO> getBlockWithMaxTransactionsCount2022_10_01() {
        Optional<String> response = httpClient.get(
                BlockchairInfinitableEndpointEnum.BLOCKS,
                Map.of(BlockchairInfinitableEndpointParameterEnum.WHERE, List.of("time(2022-10-01)"),
                        BlockchairInfinitableEndpointParameterEnum.ORDER_BY, List.of("transaction_count(desc)"),
                        BlockchairInfinitableEndpointParameterEnum.LIMIT, List.of("1"))
        );
        return response.isPresent() ? BlocksMapper.mapToBlockDTO(response.get()) : Optional.empty();
    }

    /**
     * Вопрос 9: Каким было вознаграждение за блок биткойна №300000?
     */
    public static Optional<BlockDTO> get300000BlockRewardUsd() {
        Optional<String> response = httpClient.get(
                BlockchairInfinitableEndpointEnum.BLOCKS,
                Map.of(BlockchairInfinitableEndpointParameterEnum.WHERE, List.of("id(300000)"))
        );
        return response.isPresent() ? BlocksMapper.mapToBlockDTO(response.get()) : Optional.empty();
    }

    /**
     * Вопрос 11: Какая транзакция в блоке №202020 имеет самую большую сумму перевода?
     */
    public static Optional<TransactionDTO> getMaxInputTotalUsdBlock202020Transaction() {
        Optional<String> response = httpClient.get(
                BlockchairInfinitableEndpointEnum.TRANSACTIONS,
                Map.of(BlockchairInfinitableEndpointParameterEnum.WHERE, List.of("block_id(202020)"),
                        BlockchairInfinitableEndpointParameterEnum.ORDER_BY, List.of("input_total_usd(desc)"),
                        BlockchairInfinitableEndpointParameterEnum.LIMIT, List.of("1"))
        );
        return response.isPresent() ? TransactionsMapper.mapToTransactionDTO(response.get()) : Optional.empty();
    }

    /**
     * Вопрос 12: Какая самая большая комиссия за транзакцию была в блоке BitCoin №445566?
     */
    public static Optional<TransactionDTO> getMaxFeeBlock445566Transaction() {
        Optional<String> response = httpClient.get(
                BlockchairInfinitableEndpointEnum.TRANSACTIONS,
                Map.of(BlockchairInfinitableEndpointParameterEnum.WHERE, List.of("block_id(445566)"),
                        BlockchairInfinitableEndpointParameterEnum.ORDER_BY, List.of("fee(desc)"),
                        BlockchairInfinitableEndpointParameterEnum.LIMIT, List.of("1"))
        );
        return response.isPresent() ? TransactionsMapper.mapToTransactionDTO(response.get()) : Optional.empty();
    }

    /**
     * Вопрос 13: В каком блоке BitCoin содержится транзакция с покупкой самой дорогой в мире пиццы?
     */
    public static Optional<TransactionDTO> getMostExpensivePizzaBlock() {
        Optional<String> response = httpClient.get(
                BlockchairInfinitableEndpointEnum.TRANSACTIONS,
                Map.of(BlockchairInfinitableEndpointParameterEnum.WHERE, List.of(
                        "time(2010-05-22)",
                        "output_total(1000000000000)",
                        "fee(99000000)"))
        );
        return response.isPresent() ? TransactionsMapper.mapToTransactionDTO(response.get()) : Optional.empty();
    }
}
