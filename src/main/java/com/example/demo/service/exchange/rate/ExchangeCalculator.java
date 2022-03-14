package com.example.demo.service.exchange.rate;

import com.example.demo.domain.currency.api.CurrencySnapshot;

import java.math.BigDecimal;

public interface ExchangeCalculator {

    BigDecimal calculateExchangedValue(CurrencySnapshot currency, BigDecimal valueInPln);

}
