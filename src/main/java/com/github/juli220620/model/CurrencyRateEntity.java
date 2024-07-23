package com.github.juli220620.model;

import com.github.juli220620.model.id.CurrencyRateId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "currency_rate")
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyRateEntity {

    @EmbeddedId
    private CurrencyRateId id;
    private double currRate;

    @ManyToOne
    @JoinColumn(name = "curr_id", referencedColumnName = "id", insertable = false, updatable = false)
    private CurrencyDictEntity currency;

    public CurrencyRateEntity(Long currId, LocalDate date, double currRate) {
        this.id = new CurrencyRateId(currId, date);
        this.currRate = currRate;
    }
}
