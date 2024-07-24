package com.github.juli220620;

import com.github.juli220620.controller.CurrencyRateController;
import com.github.juli220620.model.id.CurrencyRateId;
import com.github.juli220620.repo.CurrencyDictRepo;
import com.github.juli220620.repo.CurrencyRateRepo;
import com.github.juli220620.service.NBRBApiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class SimpleIntegrationTest {

    @Autowired
    private CurrencyRateController controller;
    @Autowired
    private CurrencyDictRepo currencyDictRepo;
    @Autowired
    private CurrencyRateRepo currencyRateRepo;
    @Autowired
    private NBRBApiService nbrbApiService;

    @BeforeEach
    public void loadDatesManually() {
        nbrbApiService.loadCurrenciesDict();
    }

    @Test
    public void loadRatesForDate() {
        var status = controller.loadRatesForDate(LocalDate.now());
        assertEquals(0, status.getCode());
        assertEquals("OK", status.getMessage());
    }

    @Test
    public void getRatesForDate() {
        loadRatesForDate();

        var date = LocalDate.now();
        var currencies = currencyDictRepo.findAll();
        var currency = currencies.get(new Random().nextInt(currencies.size()));
        var res = controller.getRateForDate(new CurrencyRateController.GetRateForDateRq(date, currency.getId()));
        var expected = currencyRateRepo.findById(new CurrencyRateId(currency.getId(), LocalDate.now())).orElseThrow();

        assertEquals(expected.getId().getCurrId(), res.getCurrId());
        assertEquals(date, res.getDate());
        assertEquals(expected.getCurrRate(), res.getCurrRate());
    }

}
