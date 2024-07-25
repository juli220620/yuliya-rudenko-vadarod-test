package com.github.juli220620.service;

import com.github.juli220620.model.dto.RateForDateDto;
import com.github.juli220620.model.id.CurrencyRateId;
import com.github.juli220620.repo.CurrencyDictRepo;
import com.github.juli220620.repo.CurrencyRateRepo;
import lombok.AllArgsConstructor;
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

    /**
     * Loads currency rates data for a given date to local persistence layer
     * @param date {@link LocalDate} for which currency rates should be loaded for
     * @return {@link LoadStatus} object representing whether loading was successful or not.
     */
    public LoadStatus loadRatesForDate(LocalDate date) {
       try {
           nbrbService.loadRatesForDate(date);
           return new LoadStatus();
       } catch (Exception e) {
           log.error(e.getMessage(), e);
           return new LoadStatus(e);
       }
    }

    /**
     * Returns currency exchange rate for a given currency id and date.
     * @param currId id of a currency.
     * @param date {@link LocalDate} for which rates should be returned
     * @return {@link RateForDateDto} object representing info about the currency and currency exchange rate
     * @throws RuntimeException when either given a not existing currency or no rates present
     * for given currency and date
     * @see CurrencyRateService#loadRatesForDate(LocalDate)
     */
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

    /**
     * Represents status of loading process
     * code 0 means there were no errors and loading was successful.
     * Any other code means there was an error and data was not loaded
     */
    @Getter
    @Setter
    @AllArgsConstructor
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
