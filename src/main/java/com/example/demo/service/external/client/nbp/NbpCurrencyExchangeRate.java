package com.example.demo.service.external.client.nbp;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class NbpCurrencyExchangeRate {

    private final String table;

    private final String currency;

    private final String code;

    private final List<Rate> rates;

    @JsonCreator
    public NbpCurrencyExchangeRate(@JsonProperty("table") String table,
                                   @JsonProperty("currency") String currency,
                                   @JsonProperty("code") String code,
                                   @JsonProperty("rates") List<Rate> rates) {
        this.table = table;
        this.currency = currency;
        this.code = code;
        this.rates = rates;
    }
}
