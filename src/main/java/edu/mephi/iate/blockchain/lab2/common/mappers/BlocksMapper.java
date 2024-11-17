package edu.mephi.iate.blockchain.lab2.common.mappers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.mephi.iate.blockchain.lab2.dto.blocks.BlockDTO;
import edu.mephi.iate.blockchain.lab2.dto.blocks.BlocksDTO;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@Slf4j
@UtilityClass
public class BlocksMapper {

    private static final ObjectMapper objectMapper = new ObjectMapper()
            .findAndRegisterModules()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public static Optional<BlocksDTO> mapToBlocksDTO(String response) {
        try {
            return Optional.of(objectMapper.readValue(response, BlocksDTO.class));
        } catch (JsonProcessingException e) {
            log.error("Error during mapping BlocksDTO. Message: {}", e.getMessage());
            return Optional.empty();
        }
    }

    public static Optional<BlockDTO> mapToBlockDTO(String response) {
        try {
            BlocksDTO blocksDTO = objectMapper.readValue(response, BlocksDTO.class);
            List<BlockDTO> blockDTOList = blocksDTO.getBlocks();
            if (!blockDTOList.isEmpty()) {
                return Optional.of(blockDTOList.getFirst());
            } else {
                return Optional.empty();
            }
        } catch (JsonProcessingException e) {
            log.error("Error during mapping BlockDTO. Message: {}", e.getMessage());
            return Optional.empty();
        }
    }
}
