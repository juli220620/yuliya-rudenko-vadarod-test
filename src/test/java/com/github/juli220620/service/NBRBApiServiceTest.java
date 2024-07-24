package com.github.juli220620.service;

import com.github.juli220620.model.CurrencyDictEntity;
import com.github.juli220620.model.CurrencyRateEntity;
import com.github.juli220620.model.dto.NBRBCurrencyDto;
import com.github.juli220620.model.dto.NBRBRateDto;
import com.github.juli220620.repo.CurrencyDictRepo;
import com.github.juli220620.repo.CurrencyRateRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static com.github.juli220620.service.NBRBApiService.EMPTY_CURRENCIES_ERROR_MESSAGE;
import static com.github.juli220620.service.NBRBApiService.EMPTY_RATES_ERROR_MESSAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NBRBApiServiceTest {
    private static final long USD_ID = 431L;

    @Mock
    private CurrencyDictRepo dictRepo;

    @Mock
    private CurrencyRateRepo rateRepo;

    @Spy
    @InjectMocks
    private NBRBApiService service;

    private MockedStatic<RestClient> restClientMockedStatic;

    @BeforeEach
    public void initialize() {
        restClientMockedStatic = mockStatic(RestClient.class);
    }

    @Test
    @DisplayName("Checks that actual rates loading done two times (1 for each periodicity)")
    public void loadRatesForDateLoadsTwoTimes() {
        var date = LocalDate.now();
        var list = List.of(new NBRBRateDto(USD_ID, date, 1.0D));

        setupRestClient(list);

        service.loadRatesForDate(date);

        verify(rateRepo, times(2)).save(any(CurrencyRateEntity.class));
    }

    @Test
    @DisplayName("Must fail if NBRB rates for a given date list is empty")
    public void loadRatesForDateFailOnEmptyList() {
        setupRestClient(Collections.emptyList());

        var exception = assertThrows(RuntimeException.class, () -> service.loadRatesForDate(LocalDate.now()));
        assertEquals(EMPTY_RATES_ERROR_MESSAGE, exception.getMessage());
    }

    @Test
    @DisplayName("Loads currency and saves it to repo")
    public void loadCurrenciesDictCriticalPath() {
        var list = List.of(new NBRBCurrencyDto(USD_ID, USD_ID, null, null, null, 0, LocalDate.MAX));
        setupRestClient(list);
        service.loadCurrenciesDict();

        var captor = ArgumentCaptor.forClass(CurrencyDictEntity.class);
        verify(dictRepo, times(1)).save(captor.capture());

        assertEquals(USD_ID, captor.getValue().getId());
    }

    @Test
    @DisplayName("Must fail if NBRB currencies list is empty")
    public void loadCurrenciesDictFailOnEmptyList() {
        setupRestClient(Collections.emptyList());

        var exception = assertThrows(RuntimeException.class, () -> service.loadCurrenciesDict());
        assertEquals(EMPTY_CURRENCIES_ERROR_MESSAGE, exception.getMessage());
    }

    private void setupRestClient(Object listToReturn) {
        var mock = mock(RestClient.class);

        var reqHeadersUriSpec = mock(RestClient.RequestHeadersUriSpec.class);
        doReturn(reqHeadersUriSpec).when(mock).get();
        doReturn(reqHeadersUriSpec).when(reqHeadersUriSpec).uri(anyString());

        var respSpec = mock(RestClient.ResponseSpec.class);
        doReturn(respSpec).when(reqHeadersUriSpec).retrieve();

        //noinspection unchecked
        doReturn(listToReturn).when(respSpec).body(any(ParameterizedTypeReference.class));
        restClientMockedStatic.when(RestClient::create).thenReturn(mock);
    }

    @AfterEach
    public void cleanUp() {
        restClientMockedStatic.close();
    }
}