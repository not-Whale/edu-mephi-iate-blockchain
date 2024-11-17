package edu.mephi.iate.blockchain.lab2.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContextDTO {

    @JsonSetter("total_rows")
    private long totalRows;
}
