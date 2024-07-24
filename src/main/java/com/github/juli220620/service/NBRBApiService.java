package com.github.juli220620.service;

import com.github.juli220620.model.CurrencyDictEntity;
import com.github.juli220620.model.CurrencyRateEntity;
import com.github.juli220620.model.dto.CurrencyDictDto;
import com.github.juli220620.model.dto.CurrencyRateDto;
import com.github.juli220620.model.dto.NBRBCurrencyDto;
import com.github.juli220620.model.dto.NBRBRateDto;
import com.github.juli220620.repo.CurrencyDictRepo;
import com.github.juli220620.repo.CurrencyRateRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;

@Service
@RequiredArgsConstructor
public class NBRBApiService {
    public static final String EMPTY_RATES_ERROR_MESSAGE = "Unable to fetch rates result from NBRB";
    public static final String EMPTY_CURRENCIES_ERROR_MESSAGE = "Unable to fetch currencies result from NBRB";

    private final CurrencyDictRepo currRepo;
    private final CurrencyRateRepo rateRepo;

    @Value("${app.nbrb.load-dict-uri}")
    public String loadDictUri;
    @Value("${app.nbrb.load-rates-for-date-uri-template}")
    public String loadRatesForDateUriTemplate;

    public void loadRatesForDate(LocalDate date) {
        var resultDaily = loadRates(date, "0");
        var resultMonthly = loadRates(date, "1");

        var dailyResultEmpty = resultDaily == null || resultDaily.isEmpty();
        var monthlyResultEmpty = resultMonthly == null || resultMonthly.isEmpty();

        if (dailyResultEmpty || monthlyResultEmpty) {
            throw new RuntimeException(EMPTY_RATES_ERROR_MESSAGE);
        }

        Stream.concat(resultMonthly.stream(), resultDaily.stream())
                .map(nbrbRateDto -> new CurrencyRateDto(
                        nbrbRateDto.getCurrId(),
                        nbrbRateDto.getDate(),
                        nbrbRateDto.getCurrOfficialRate()
                )).forEach(it -> rateRepo.save(
                        new CurrencyRateEntity(
                                it.getCurrId(),
                                it.getDate(),
                                it.getCurrRate()
                        )
                ));
    }

    private List<NBRBRateDto> loadRates(LocalDate date, String periodicity) {
        return RestClient.create().get()
                .uri(String.format(loadRatesForDateUriTemplate, periodicity, date.format(ISO_LOCAL_DATE)))
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    public void loadCurrenciesDict() {
        var result = RestClient.create().get()
                .uri(loadDictUri)
                .retrieve()
                .body(new ParameterizedTypeReference<List<NBRBCurrencyDto>>() {});

        if (result == null || result.isEmpty()) {
            throw new RuntimeException(EMPTY_CURRENCIES_ERROR_MESSAGE);
        }

        result.stream()
                .filter(it -> it.getCurDateEnd().isAfter(LocalDate.now()))
                .map(it -> new CurrencyDictDto(
                        it.getCurId(),
                        it.getCurCode(),
                        it.getCurAbbreviation(),
                        it.getCurScale(),
                        it.getCurName(),
                        it.getCurNameMulti())
                ).forEach(it -> currRepo.save(new CurrencyDictEntity(
                        it.getId(),
                        it.getCode(),
                        it.getAbbreviation(),
                        it.getScale(),
                        it.getName(),
                        it.getNameMulti())
                ));
    }
}
