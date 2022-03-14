package com.example.demo.test.utils;

import com.example.demo.domain.account.AccountTestCleaner;
import com.example.demo.domain.client.ClientTestCleaner;
import com.example.demo.domain.currency.CurrencyTestCleaner;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestCleaner {

    private final CurrencyTestCleaner currencyTestCleaner;

    private final ClientTestCleaner clientTestCleaner;

    private final AccountTestCleaner accountTestCleaner;

    public void cleanDatabase() {
        accountTestCleaner.cleanAccountTable();
        clientTestCleaner.cleanClientTable();
        currencyTestCleaner.cleanCurrencyTable();
    }

}
