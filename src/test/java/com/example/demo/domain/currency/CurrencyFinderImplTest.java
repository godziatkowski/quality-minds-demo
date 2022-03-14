package com.example.demo.domain.currency;

import com.example.demo.domain.currency.api.CurrencySnapshot;
import com.example.demo.test.utils.IntegrationTest;
import com.example.demo.test.utils.TestCleaner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@IntegrationTest
class CurrencyFinderImplTest {

    @Autowired
    private CurrencyFinderImpl currencyFinder;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private TestCleaner testCleaner;

    @AfterEach
    void tearDown() {
        testCleaner.cleanDatabase();
    }

    @Test
    void findCurrencyByCode_shouldFindCurrency() {
        //given
        Currency savedCurrency = currencyRepository.save(Currency.builder()
                                                                 .name("demo")
                                                                 .isoCode("DEM")
                                                                 .build());
        //when
        CurrencySnapshot foundCurrency = currencyFinder.findCurrencyByCode(savedCurrency.getIsoCode());
        //then
        assertThat(foundCurrency).isEqualTo(savedCurrency.toSnapshot());
    }

    @Test
    void findCurrencyByCode_shouldThrowNotFound_whenCurrencyNotSupported() {
        //given
        String isoCode = "USD";
        //when
        Throwable thrown = catchThrowable(() -> currencyFinder.findCurrencyByCode(isoCode));
        //then
        assertThat(thrown).isNotNull()
                          .isInstanceOf(ResponseStatusException.class);

        assertThat(((ResponseStatusException) thrown).getStatus()).isEqualTo(HttpStatus.NOT_FOUND);

    }
}