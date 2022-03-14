package com.example.demo.common.utils;

import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.math.RoundingMode;

@UtilityClass
public class DefaultCurrencyScale {

    public static final int DEFAULT_SCALE = 4;

    public static final RoundingMode DEFAULT_ROUNDING_MODE = RoundingMode.HALF_EVEN;

    public static BigDecimal useDefaultScaleAndRounding(BigDecimal value) {
        return value.setScale(DEFAULT_SCALE, DEFAULT_ROUNDING_MODE);
    }

}
