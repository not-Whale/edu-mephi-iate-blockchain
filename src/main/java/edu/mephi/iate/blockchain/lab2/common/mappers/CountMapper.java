package edu.mephi.iate.blockchain.lab2.common.mappers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.mephi.iate.blockchain.lab2.dto.counts.CountDTO;
import edu.mephi.iate.blockchain.lab2.dto.counts.CountsDTO;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@Slf4j
@UtilityClass
public class CountMapper {

    private static final ObjectMapper objectMapper = new ObjectMapper()
            .findAndRegisterModules()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public static Optional<CountDTO> mapToCountDTO(String response) {
        try {
            CountsDTO countsDTO = objectMapper.readValue(response, CountsDTO.class);
            List<CountDTO> countDTOList = countsDTO.getCounts();
            if (!countDTOList.isEmpty()) {
                return Optional.of(countDTOList.getFirst());
            } else {
                return Optional.empty();
            }
        } catch (JsonProcessingException e) {
            log.error("Error during mapping countDTO. Message: {}", e.getMessage());
            return Optional.empty();
        }
    }
}
