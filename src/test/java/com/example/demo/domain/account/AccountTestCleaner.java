package com.example.demo.domain.account;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountTestCleaner {
    private final AccountRepository accountRepository;

    public void cleanAccountTable() {
        accountRepository.deleteAll();
    }
}
