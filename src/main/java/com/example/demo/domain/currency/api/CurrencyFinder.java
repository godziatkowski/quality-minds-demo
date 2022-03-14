package com.example.demo.domain.currency.api;

public interface CurrencyFinder {

    CurrencySnapshot findCurrencyByCode(String currencyCode);
}
