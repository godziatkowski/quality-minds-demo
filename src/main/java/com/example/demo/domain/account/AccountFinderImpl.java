package com.example.demo.domain.account;

import com.example.demo.domain.account.api.AccountFinder;
import com.example.demo.domain.account.api.AccountSnapshot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.example.demo.common.utils.ResponseStatusExceptionGenerator.notFoundException;

@Service
@RequiredArgsConstructor
public class AccountFinderImpl implements AccountFinder {

    private static final String ACCOUNT_NOT_FOUND_MESSAGE = "Account with number %s doesn't exist";

    private final AccountRepository accountRepository;

    @Override
    public AccountSnapshot getAccount(UUID accountId) {
        return accountRepository.findById(accountId)
                                .map(Account::toSnapshot)
                                .orElseThrow(() -> notFoundException(ACCOUNT_NOT_FOUND_MESSAGE.formatted(accountId)));
    }


}
