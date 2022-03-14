package com.example.demo.service.exchange.rate.nbp;

import com.example.demo.common.utils.DefaultCurrencyScale;
import com.example.demo.domain.currency.api.CurrencySnapshot;
import com.example.demo.service.exchange.rate.ExchangeCalculator;
import com.example.demo.service.external.client.nbp.NbpApiClient;
import com.example.demo.service.external.client.nbp.NbpCurrencyExchangeRate;
import com.example.demo.service.external.client.nbp.Rate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

import static com.example.demo.common.utils.DefaultCurrencyScale.DEFAULT_ROUNDING_MODE;
import static com.example.demo.common.utils.DefaultCurrencyScale.useDefaultScaleAndRounding;
import static com.example.demo.common.utils.ResponseStatusExceptionGenerator.notFoundException;

@Slf4j
@Service
@RequiredArgsConstructor
public class NbpExchangeCalculator implements ExchangeCalculator {

    static final String NBP_NO_RESPONSE = "NBP didn't return correct exchange rate for %s";

    private final NbpApiClient nbpApiClient;

    public BigDecimal calculateExchangedValue(CurrencySnapshot currency, BigDecimal valueInPln) {
        BigDecimal latestExchangeRate = getLatestExchangeRate(currency);
        log.info("Calculating exchanged value of <{}> PLN to <{}> using exchange rate <{}>",
                 valueInPln, currency.getIsoCode(), latestExchangeRate);
        BigDecimal scaledPlnValue = useDefaultScaleAndRounding(valueInPln);
        return scaledPlnValue.divide(latestExchangeRate, DEFAULT_ROUNDING_MODE);
    }

    private BigDecimal getLatestExchangeRate(CurrencySnapshot currency) {
        Optional<NbpCurrencyExchangeRate> nbpExchangeRate = getNbpExchangeRateForCurrency(currency);
        List<Rate> exchangeRates = nbpExchangeRate.map(NbpCurrencyExchangeRate::getRates)
                                                  .orElse(List.of());
        return exchangeRates.stream()
                            .map(Rate::getMid)
                            .map(BigDecimal::new)
                            .map(DefaultCurrencyScale::useDefaultScaleAndRounding)
                            .findFirst()
                            .orElseThrow(() -> notFoundException(NBP_NO_RESPONSE.formatted(currency.getName())));
    }

    private Optional<NbpCurrencyExchangeRate> getNbpExchangeRateForCurrency(CurrencySnapshot currency) {

        Optional<NbpCurrencyExchangeRate> response
            = nbpApiClient.getTodayExchangeRate(currency.getIsoCode())
                          .or(() -> nbpApiClient.getLastExchangeRate(currency.getIsoCode(), 1));
        log.debug("NBP response for <{}> exchange rate is <{}>", currency, response);
        return response;
    }
}
