package edu.mephi.iate.blockchain.lab2.dto.outputs;

import com.fasterxml.jackson.annotation.JsonSetter;
import edu.mephi.iate.blockchain.lab2.dto.ContextDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OutputsDTO {

    @JsonSetter("data")
    private List<OutputDTO> outputs;

    @JsonSetter("context")
    private ContextDTO context;
}
