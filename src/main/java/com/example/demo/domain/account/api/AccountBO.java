package com.example.demo.domain.account.api;

import java.math.BigDecimal;
import java.util.UUID;

public interface AccountBO {

    UUID openAccountForUser(UUID userId);

    AccountSnapshot credit(UUID accountId, BigDecimal amount);

    AccountSnapshot debit(UUID accountId, BigDecimal amount);
}
