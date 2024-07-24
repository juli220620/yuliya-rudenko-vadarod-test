package com.github.juli220620.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class NBRBCurrencyDto {

    @JsonProperty("Cur_ID")
    private long curId;
    @JsonProperty("Cur_Code")
    private long curCode;
    @JsonProperty("Cur_Abbreviation")
    private String curAbbreviation;
    @JsonProperty("Cur_Name")
    private String curName;
    @JsonProperty("Cur_NameMulti")
    private String curNameMulti;
    @JsonProperty("Cur_Scale")
    private int curScale;

    @JsonProperty("Cur_DateEnd")
    private LocalDate curDateEnd;
}
