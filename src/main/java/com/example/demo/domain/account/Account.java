package com.example.demo.domain.account;

import com.example.demo.domain.account.api.AccountSnapshot;
import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.UUID;

import static com.example.demo.common.utils.DefaultCurrencyScale.useDefaultScaleAndRounding;

@Getter
@Entity
@NoArgsConstructor
class Account {

    @Id
    private UUID accountId;

    @NotNull
    private UUID ownerId;

    @NotNull
    private BigDecimal balance;

    @Builder
    public Account(UUID accountId, UUID ownerId) {
        this.accountId = accountId;
        this.ownerId = ownerId;
        this.balance = useDefaultScaleAndRounding(new BigDecimal("0.00"));
    }

    void credit(BigDecimal amount) {
        balance = balance.add(amount);
    }

    void debit(BigDecimal amount) {
        balance = balance.subtract(amount);
    }

    AccountSnapshot toSnapshot() {
        return AccountSnapshot.builder()
                              .accountId(accountId)
                              .ownerId(ownerId)
                              .balance(balance)
                              .build();
    }
}
