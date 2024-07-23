package com.github.juli220620.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyDictDto {

    private Long id;
    private Long code;
    private String abbreviation;
    private Integer scale;
    private String name;
    private String nameMulti;
}
