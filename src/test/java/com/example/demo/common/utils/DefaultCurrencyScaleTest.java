package com.example.demo.common.utils;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static com.example.demo.common.utils.DefaultCurrencyScale.useDefaultScaleAndRounding;
import static org.assertj.core.api.Assertions.assertThat;

class DefaultCurrencyScaleTest {

    @ParameterizedTest
    @MethodSource("roundingValues")
    void useDefaultScaleAndRounding_shouldScaleAndRoundValue(BigDecimal initialValue, BigDecimal expectedValue) {
        //when
        BigDecimal result = useDefaultScaleAndRounding(initialValue);
        //then
        assertThat(result).isEqualTo(expectedValue);
    }

    private static Stream<Arguments> roundingValues() {
        return Stream.of(
            Arguments.of(new BigDecimal("11.11111111"), new BigDecimal("11.1111")),
            Arguments.of(new BigDecimal("11.11114"), new BigDecimal("11.1111")),
            Arguments.of(new BigDecimal("11.11115"), new BigDecimal("11.1112")),
            Arguments.of(new BigDecimal("11.11116"), new BigDecimal("11.1112")),
            Arguments.of(new BigDecimal("11.111"), new BigDecimal("11.1110"))
        );
    }
}