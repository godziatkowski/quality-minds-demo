package com.example.demo.domain.currency;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrencyTestCleaner {
    private final CurrencyRepository currencyRepository;

    public void cleanCurrencyTable() {
        currencyRepository.deleteAll();
    }
}
