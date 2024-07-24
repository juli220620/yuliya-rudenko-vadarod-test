package com.github.juli220620.service;

import com.github.juli220620.model.dto.RateForDateDto;
import com.github.juli220620.model.id.CurrencyRateId;
import com.github.juli220620.repo.CurrencyDictRepo;
import com.github.juli220620.repo.CurrencyRateRepo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyRateService {

    private final CurrencyRateRepo rateRepo;
    private final CurrencyDictRepo currRepo;
    private final NBRBApiService nbrbService;

    public LoadStatus loadRatesForDate(LocalDate date) {
       try {
           nbrbService.loadRatesForDate(date);
           return new LoadStatus();
       } catch (Exception e) {
           log.error(e.getMessage(), e);
           return new LoadStatus(e);
       }
    }

    public RateForDateDto getRateForDate(Long currId, LocalDate date) {
        var currEntity = currRepo.findById(currId)
                .orElseThrow(() -> new RuntimeException("No such currency"));
        var entity = rateRepo.findById(new CurrencyRateId(currId, date))
                .orElseThrow(() -> new RuntimeException("No data available"));

        return new RateForDateDto(
                currId,
                currEntity.getCode(),
                currEntity.getAbbreviation(),
                currEntity.getScale(),
                currEntity.getName(),
                date,
                entity.getCurrRate()
        );
    }

    @Getter
    @Setter
    public static class LoadStatus {

        private int code;
        private String message;

        public LoadStatus() {
            code = 0;
            message = "OK";
        }

        public LoadStatus(Exception e) {
            code = 500;
            message = e.getMessage();
        }
    }
}
