package com.github.juli220620.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class NBRBRateDto {

    @JsonProperty("Cur_ID")
    private Long currId;
    @JsonProperty("Date")
    private LocalDate date;
    @JsonProperty("Cur_OfficialRate")
    private double currOfficialRate;
}
