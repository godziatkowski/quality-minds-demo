package com.example.demo.domain.account.api;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

import static com.example.demo.common.utils.DefaultCurrencyScale.useDefaultScaleAndRounding;

@Getter
@EqualsAndHashCode
public class AccountSnapshot {

    private final UUID ownerId;

    private final UUID accountId;

    private final BigDecimal balance;

    @Builder
    public AccountSnapshot(UUID ownerId, UUID accountId, BigDecimal balance) {
        this.ownerId = ownerId;
        this.accountId = accountId;
        this.balance = useDefaultScaleAndRounding(balance);
    }
}
