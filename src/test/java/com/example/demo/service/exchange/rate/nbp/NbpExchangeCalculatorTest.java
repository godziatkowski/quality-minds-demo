package com.example.demo.service.exchange.rate.nbp;

import com.example.demo.domain.currency.api.CurrencySnapshot;
import com.example.demo.service.external.client.nbp.NbpApiClient;
import com.example.demo.service.external.client.nbp.NbpCurrencyExchangeRate;
import com.example.demo.service.external.client.nbp.Rate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Stream;

import static com.example.demo.common.utils.DefaultCurrencyScale.useDefaultScaleAndRounding;
import static com.example.demo.service.exchange.rate.nbp.NbpExchangeCalculator.NBP_NO_RESPONSE;
import static com.example.demo.service.external.client.nbp.NbpApiResponseGenerator.sampleCurrencyExchangeRate;
import static com.example.demo.service.external.client.nbp.NbpApiResponseGenerator.sampleRate;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
class NbpExchangeCalculatorTest {

    private static final Random RANDOM = new Random();

    private static final CurrencySnapshot EURO = CurrencySnapshot.builder().isoCode("EUR").name("euro").build();

    @Mock
    private NbpApiClient nbpApiClient;

    private NbpExchangeCalculator nbpExchangeRateCalculator;

    @BeforeEach
    void setUp() {
        nbpExchangeRateCalculator = new NbpExchangeCalculator(nbpApiClient);
    }

    @ParameterizedTest
    @MethodSource("exchangeValues")
    void calculateExchangeRate_shouldReturnCorrectValue_usingTodayExchangeRate(BigDecimal valueInPln,
                                                                              double exchangeRate,
                                                                              BigDecimal expectedValueInCurrency) {
        //given
        Rate rate = sampleRate(exchangeRate).build();
        NbpCurrencyExchangeRate nbpCurrencyExchangeRate = sampleCurrencyExchangeRate(List.of(rate)).build();

        given(nbpApiClient.getTodayExchangeRate(EURO.getIsoCode()))
            .willReturn(Optional.of(nbpCurrencyExchangeRate));
        //when
        BigDecimal exchangedValue = nbpExchangeRateCalculator.calculateExchangedValue(EURO, valueInPln);
        //then
        assertThat(exchangedValue).isEqualTo(expectedValueInCurrency);
    }

    @ParameterizedTest
    @MethodSource("exchangeValues")
    void calculateExchangeRate_shouldReturnCorrectValue_usingLastExchangeRate(BigDecimal valueInPln,
                                                                             double exchangeRate,
                                                                             BigDecimal expectedValueInCurrency) {
        //given
        Rate rate = sampleRate(exchangeRate).build();
        NbpCurrencyExchangeRate nbpCurrencyExchangeRate = sampleCurrencyExchangeRate(List.of(rate)).build();

        given(nbpApiClient.getTodayExchangeRate(EURO.getIsoCode()))
            .willReturn(Optional.empty());
        given(nbpApiClient.getLastExchangeRate(EURO.getIsoCode(), 1))
            .willReturn(Optional.of(nbpCurrencyExchangeRate));
        //when
        BigDecimal exchangedValue = nbpExchangeRateCalculator.calculateExchangedValue(EURO, valueInPln);
        //then
        assertThat(exchangedValue).isEqualTo(expectedValueInCurrency);
    }

    @Test
    void calculateExchangeRate_shouldThrowException_whenNbpDidntRespond() {
        //given
        CurrencySnapshot currency = CurrencySnapshot.builder().isoCode("EUR").build();
        BigDecimal valueInPln = new BigDecimal(String.valueOf(RANDOM.nextDouble()));

        given(nbpApiClient.getTodayExchangeRate(currency.getIsoCode()))
            .willReturn(Optional.empty());
        given(nbpApiClient.getLastExchangeRate(currency.getIsoCode(), 1))
            .willReturn(Optional.empty());

        //when
        Throwable thrown = catchThrowable(
            () -> nbpExchangeRateCalculator.calculateExchangedValue(currency, valueInPln));

        //then
        assertThat(thrown).isNotNull()
                          .isInstanceOf(ResponseStatusException.class);
        ResponseStatusException exception = (ResponseStatusException) thrown;
        assertThat(exception.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(exception.getReason()).isEqualTo(NBP_NO_RESPONSE.formatted(currency.getName()));

    }

    private static Stream<Arguments> exchangeValues() {
        return Stream.of(
            Arguments.of(new BigDecimal(500), 4.6279d, useDefaultScaleAndRounding(new BigDecimal("108.0404"))),
            Arguments.of(new BigDecimal(500), 4.7924d, useDefaultScaleAndRounding(new BigDecimal("104.3319"))),
            Arguments.of(new BigDecimal(500), 4.4916d, useDefaultScaleAndRounding(new BigDecimal("111.3189"))),
            Arguments.of(new BigDecimal("4.5821"), 4.5821d, useDefaultScaleAndRounding(new BigDecimal("1"))),
            Arguments.of(new BigDecimal("4.58215"), 4.5821d, useDefaultScaleAndRounding(new BigDecimal("1"))),
            Arguments.of(new BigDecimal(1), 4.5821d, useDefaultScaleAndRounding(new BigDecimal("0.2182"))),
            Arguments.of(new BigDecimal(5), 4.5821d, useDefaultScaleAndRounding(new BigDecimal("1.0912"))),
            Arguments.of(new BigDecimal(0), 4.5821d, useDefaultScaleAndRounding(new BigDecimal("0.0000")))
        );
    }
}