package com.github.juli220620.controller;

import com.github.juli220620.model.dto.RateForDateDto;
import com.github.juli220620.service.CurrencyRateService;
import com.github.juli220620.service.LoadStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/rate")
@RequiredArgsConstructor
public class CurrencyRateController {

    private final CurrencyRateService service;

    @PostMapping("/{date}")
    public LoadStatus loadRatesForDate(@PathVariable LocalDate date) {
        return service.loadRatesForDate(date);
    }

    @GetMapping()
    public RateForDateDto getRateForDate(@RequestBody GetRateForDateRq rq) {
        return service.getRateForDate(rq.getCurrId(), rq.getDate());
    }

    @Getter
    public static class GetRateForDateRq {
        private LocalDate date;
        private long currId;
    }
}
