package com.example.demo.domain.account;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class AccountTest {

    @Test
    void newAccount_shouldHaveBalanceEqualToZero() {
        //given
        Account.AccountBuilder builder = Account.builder()
                                                .ownerId(UUID.randomUUID())
                                                .accountId(UUID.randomUUID());
        //when
        Account account = builder.build();
        //then
        assertThat(account.getBalance()).isEqualTo("0.0000");
    }

    @Test
    void credit_shouldIncreaseAccountBalance() {
        //given
        Account account = Account.builder()
                                 .ownerId(UUID.randomUUID())
                                 .accountId(UUID.randomUUID())
                                 .build();
        BigDecimal amountToIncrease = new BigDecimal("10.0");
        BigDecimal expectedValue = account.getBalance().add(amountToIncrease);
        //when
        account.credit(amountToIncrease);
        //then
        assertThat(account.getBalance()).isEqualTo(expectedValue);
    }

    @Test
    void debit_shouldDecreaseAccountBalance() {
        //given
        Account account = Account.builder()
                                 .ownerId(UUID.randomUUID())
                                 .accountId(UUID.randomUUID())
                                 .build();
        BigDecimal amountToDecrease = new BigDecimal("10.0");
        BigDecimal expectedValue = account.getBalance().subtract(amountToDecrease);
        //when
        account.debit(amountToDecrease);
        //then
        assertThat(account.getBalance()).isEqualTo(expectedValue);
    }

}