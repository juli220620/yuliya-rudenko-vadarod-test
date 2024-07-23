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
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NBRBApiService {
    public static final String LOAD_DICT_URI = "https://api.nbrb.by/exrates/currencies";
    public static final String LOAD_RATES_FOR_DATE_TEMPLATE = "https://api.nbrb.by/exrates/rates?periodicity=0&ondate=";

    private final CurrencyDictRepo currRepo;
    private final CurrencyRateRepo rateRepo;

    public void loadRatesForDate(LocalDate date) {
        RestClient client = RestClient.create();

        List<NBRBRateDto> result;

        try {
            result = client.get()
                    .uri(LOAD_RATES_FOR_DATE_TEMPLATE + date.format(DateTimeFormatter.ISO_LOCAL_DATE))
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (result == null || result.isEmpty()) {
            throw new RuntimeException("Unable to fetch result from NBRB");
        }

        result.stream().map(nbrbRateDto -> new CurrencyRateDto(
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

    public void loadCurrenciesDict() {
        RestClient client = RestClient.create(LOAD_DICT_URI);

        List<NBRBCurrencyDto> result;

        try {
            result = client.get()
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });
        } catch (Exception e) {
            throw new RuntimeException(e);
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
