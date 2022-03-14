package com.example.demo.test.utils;

import com.example.demo.domain.client.ClientTestCleaner;
import com.example.demo.domain.currency.CurrencyTestCleaner;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestCleaner {

    private final CurrencyTestCleaner currencyTestCleaner;

    private final ClientTestCleaner clientTestCleaner;

    public void cleanDatabase() {
        clientTestCleaner.cleanClientTable();
        currencyTestCleaner.cleanCurrencyTable();
    }

}
