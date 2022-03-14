package com.example.demo.domain.account.api;

import java.util.UUID;

public interface AccountFinder {

    AccountSnapshot getAccount(UUID accountId);
}
