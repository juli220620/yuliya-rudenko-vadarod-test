package com.github.juli220620.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RateForDateDto {

    private Long currId;
    private Long currCode;
    private String currAbbreviation;
    private Integer scale;
    private String currName;
    private LocalDate date;
    private double currRate;
}
