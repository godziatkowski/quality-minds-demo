package com.example.demo.domain.account;

import com.example.demo.domain.account.api.AccountSnapshot;
import com.example.demo.test.utils.IntegrationTest;
import com.example.demo.test.utils.TestCleaner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@IntegrationTest
class AccountFinderImplTest {

    @Autowired
    private AccountFinderImpl accountFinder;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TestCleaner testCleaner;

    @AfterEach
    void tearDown() {
        testCleaner.cleanDatabase();
    }

    @Test
    void getAccount_shouldReturnAccountSnapshot() {
        //given
        Account account = accountRepository.save(Account.builder()
                                                        .ownerId(UUID.randomUUID())
                                                        .accountId(UUID.randomUUID())
                                                        .build());
        //when
        AccountSnapshot foundAccount = accountFinder.getAccount(account.getAccountId());
        //then
        assertThat(foundAccount).isEqualTo(account.toSnapshot());
    }

    @Test
    void getAccount_shouldThrowNotFoundException_whenAccountDoesntExist() {
        //given
        UUID accountId = UUID.randomUUID();
        //when
        Throwable thrown = catchThrowable(() -> accountFinder.getAccount(accountId));
        //then
        assertThat(thrown).isNotNull()
                          .isInstanceOf(ResponseStatusException.class);

        assertThat(((ResponseStatusException) thrown).getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}