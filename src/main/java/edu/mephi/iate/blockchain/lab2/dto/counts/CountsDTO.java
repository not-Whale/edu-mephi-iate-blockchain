package edu.mephi.iate.blockchain.lab2.dto.counts;

import com.fasterxml.jackson.annotation.JsonSetter;
import edu.mephi.iate.blockchain.lab2.dto.ContextDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountsDTO {

    @JsonSetter("data")
    private List<CountDTO> counts;

    @JsonSetter("context")
    private ContextDTO context;
}
