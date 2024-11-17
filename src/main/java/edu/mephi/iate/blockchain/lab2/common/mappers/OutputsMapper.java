package edu.mephi.iate.blockchain.lab2.common.mappers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.mephi.iate.blockchain.lab2.dto.outputs.OutputDTO;
import edu.mephi.iate.blockchain.lab2.dto.outputs.OutputsDTO;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@Slf4j
@UtilityClass
public class OutputsMapper {

    private static final ObjectMapper objectMapper = new ObjectMapper()
            .findAndRegisterModules()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);


    public static Optional<OutputsDTO> mapToOutputsDTO(String response) {
        try {
            return Optional.of(objectMapper.readValue(response, OutputsDTO.class));
        } catch (JsonProcessingException e) {
            log.error("Error during mapping OutputsDTO. Message: {}", e.getMessage());
            return Optional.empty();
        }
    }

    public static Optional<OutputDTO> mapToOutputDTO(String response) {
        try {
            OutputsDTO outputsDTO = objectMapper.readValue(response, OutputsDTO.class);
            List<OutputDTO> outputDTOList = outputsDTO.getOutputs();
            if (!outputDTOList.isEmpty()) {
                return Optional.of(outputDTOList.getFirst());
            } else {
                return Optional.empty();
            }
        } catch (JsonProcessingException e) {
            log.error("Error during mapping OutputDTO. Message: {}", e.getMessage());
            return Optional.empty();
        }
    }
}
