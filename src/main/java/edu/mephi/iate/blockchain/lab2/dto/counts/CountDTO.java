package edu.mephi.iate.blockchain.lab2.dto.counts;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountDTO {

    @JsonSetter("count()")
    private long count;
}
