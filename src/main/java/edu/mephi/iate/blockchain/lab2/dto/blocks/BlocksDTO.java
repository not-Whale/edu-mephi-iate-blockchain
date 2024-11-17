package edu.mephi.iate.blockchain.lab2.dto.blocks;

import com.fasterxml.jackson.annotation.JsonSetter;
import edu.mephi.iate.blockchain.lab2.dto.ContextDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlocksDTO {

    @JsonSetter("data")
    private List<BlockDTO> blocks;

    @JsonSetter("context")
    private ContextDTO context;
}
