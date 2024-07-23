package com.github.juli220620;

import com.github.juli220620.service.NBRBApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CurrencyDataAutoLoader {

    private final NBRBApiService service;

    @EventListener(ApplicationReadyEvent.class)
    public void loadCurrencyDictData() {
        service.loadCurrenciesDict();
    }
}
