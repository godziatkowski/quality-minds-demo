package com.example.demo.domain.account;

import com.example.demo.domain.account.api.AccountBO;
import com.example.demo.domain.account.api.AccountSnapshot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

import static com.example.demo.common.utils.ResponseStatusExceptionGenerator.notFoundException;

@Service
@Transactional
@RequiredArgsConstructor
class AccountBOImpl implements AccountBO {

    private static final String ACCOUNT_NOT_FOUND = "Account with id %s not found";

    private final AccountRepository accountRepository;

    public UUID openAccountForUser(UUID userId) {
        Account newAccount = Account.builder()
                                    .ownerId(userId)
                                    .accountId(UUID.randomUUID())
                                    .build();
        newAccount = accountRepository.save(newAccount);
        return newAccount.toSnapshot().getAccountId();
    }

    public AccountSnapshot credit(UUID accountId, BigDecimal amount) {
        Account account = findAccount(accountId);
        account.credit(amount);
        account = accountRepository.save(account);
        return account.toSnapshot();
    }

    @Override
    public AccountSnapshot debit(UUID accountId, BigDecimal amount) {
        Account account = findAccount(accountId);
        account.debit(amount);
        account = accountRepository.save(account);
        return account.toSnapshot();
    }

    private Account findAccount(UUID accountId) {
        return accountRepository.findById(accountId)
                                .orElseThrow(() -> notFoundException(ACCOUNT_NOT_FOUND.formatted(accountId)));
    }
}
