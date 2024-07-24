package com.github.juli220620.service;

import com.github.juli220620.model.CurrencyDictEntity;
import com.github.juli220620.model.CurrencyRateEntity;
import com.github.juli220620.model.id.CurrencyRateId;
import com.github.juli220620.repo.CurrencyDictRepo;
import com.github.juli220620.repo.CurrencyRateRepo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CurrencyRateServiceTest {
    private static final long USD_ID = 431L;

    @Mock
    private NBRBApiService nbrbApiService;

    @Mock
    private CurrencyDictRepo currencyDictRepo;
    @Mock
    private CurrencyRateRepo currencyRateRepo;

    @InjectMocks
    private CurrencyRateService service;

    @Test
    @DisplayName("Loading rates for given date calls NBRB api and returns OK")
    public void loadRatesForDateCriticalPath() {
        var date = LocalDate.now();
        var res = service.loadRatesForDate(date);

        verify(nbrbApiService, times(1)).loadRatesForDate(date);
        assertEquals(0, res.getCode());
        assertEquals("OK", res.getMessage());
    }

    @Test
    @DisplayName("Error message and code not 0 on internal exception")
    public void loadRatesForDateErrorCodeOnException() {
        var date = LocalDate.now();
        doThrow(new RuntimeException()).when(nbrbApiService).loadRatesForDate(date);

        var res = service.loadRatesForDate(date);

        verify(nbrbApiService, times(1)).loadRatesForDate(date);

        assertNotEquals(0, res.getCode());
        assertNotEquals("OK", res.getMessage());
    }

    @Test
    @DisplayName("Correct rates are returned for given date")
    public void getRatesForDateCriticalPath() {
        var date = LocalDate.now();
        var ratesEntity = new CurrencyRateEntity(USD_ID, date, 1.0D);
        var currEntity = new CurrencyDictEntity(USD_ID, USD_ID, null, 1, null, null);

        doReturn(Optional.of(ratesEntity)).when(currencyRateRepo).findById(any(CurrencyRateId.class));
        doReturn(Optional.of(currEntity)).when(currencyDictRepo).findById(USD_ID);

        var res = service.getRateForDate(USD_ID, date);

        assertEquals(ratesEntity.getId().getCurrId(), res.getCurrId());
        assertEquals(date, res.getDate());
        assertEquals(ratesEntity.getCurrRate(), res.getCurrRate());
    }

}