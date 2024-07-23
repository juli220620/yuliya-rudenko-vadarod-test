package com.github.juli220620.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "currency_dict")
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyDictEntity {

    @Id
    private Long id;
    private long code;
    private String abbreviation;
    private int scale;
    private String name;
    private String nameMulti;
}
