package com.example.demo.test.utils;

import com.example.demo.domain.currency.CurrencyTestCleaner;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestCleaner {

    private final CurrencyTestCleaner currencyTestCleaner;

    public void cleanDatabase() {

        currencyTestCleaner.cleanCurrencyTable();
    }

}
