package com.github.juli220620;

import com.github.juli220620.service.NBRBApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CurrencyDataAutoLoader {

    private final NBRBApiService service;

    @EventListener(ApplicationReadyEvent.class)
    public void loadCurrencyDictData() {
        try {
            service.loadCurrenciesDict();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
