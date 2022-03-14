package com.example.demo.domain.currency.api;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode
public class CurrencySnapshot {

    private final Long id;

    private final String isoCode;

    private final String name;

}
