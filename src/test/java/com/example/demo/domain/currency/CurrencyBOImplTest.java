package com.example.demo.domain.currency;

import com.example.demo.test.utils.IntegrationTest;
import com.example.demo.test.utils.TestCleaner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolationException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@IntegrationTest
class CurrencyBOImplTest {
    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private CurrencyBOImpl currencyBO;

    @Autowired
    private TestCleaner testCleaner;

    @AfterEach
    void tearDown() {
        testCleaner.cleanDatabase();
    }

    @Test
    void supportCurrency_shouldCreateCurrencyEntity() {
        //given
        String name = "demo";
        String isoCode = "DEM";
        //when
        Long id = currencyBO.supportCurrency(name, isoCode);
        //then
        Optional<Currency> currency = currencyRepository.findById(id);
        assertThat(currency).isPresent();
        assertThat(currency.map(Currency::getName).orElseThrow()).isEqualTo(name);
        assertThat(currency.map(Currency::getIsoCode).orElseThrow()).isEqualTo(isoCode);
    }

    @ParameterizedTest
    @ValueSource(strings = { "DE", "DEMO", "" })
    void supportCurrency_shouldRejectInvalidIsoCode(String isoCode) {
        //given
        String name = "demo";
        //when
        Throwable thrown = catchThrowable(() -> currencyBO.supportCurrency(name, isoCode));
        //then
        assertThat(thrown).isNotNull()
                          .hasRootCauseInstanceOf(ConstraintViolationException.class);
    }

}