package com.example.demo.service.external.client.nbp;

import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.util.List;

@UtilityClass
public class NbpApiResponseGenerator {
    public static NbpCurrencyExchangeRate.NbpCurrencyExchangeRateBuilder sampleCurrencyExchangeRate(List<Rate> rates) {
        return NbpCurrencyExchangeRate.builder()
                                      .table("A")
                                      .currency("euro")
                                      .code("EUR")
                                      .rates(rates);
    }

    public static Rate.RateBuilder sampleRate(double rateValue) {
        return Rate.builder()
                   .no("049/A/NBP/2022")
                   .effectiveDate(LocalDate.of(2022, 3, 11))
                   .mid(String.valueOf(rateValue));
    }
}
