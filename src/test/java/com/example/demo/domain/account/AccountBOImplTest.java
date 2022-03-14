package com.example.demo.domain.account;

import com.example.demo.domain.account.api.AccountSnapshot;
import com.example.demo.test.utils.IntegrationTest;
import com.example.demo.test.utils.TestCleaner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@IntegrationTest
class AccountBOImplTest {

    @Autowired
    private AccountBOImpl accountBo;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TestCleaner testCleaner;

    @AfterEach
    void tearDown() {
        testCleaner.cleanDatabase();
    }

    @Test
    void openAccountForUser_shouldCreateNewAccountForUserWithId() {
        //given
        UUID clientId = UUID.randomUUID();
        //when
        UUID accountId = accountBo.openAccountForUser(clientId);
        //then
        assertThat(accountId).isNotNull();
        Optional<Account> createdAccount = accountRepository.findById(accountId);
        assertThat(createdAccount).isPresent();
        assertThat(createdAccount.map(Account::getOwnerId).orElseThrow()).isEqualTo(clientId);
        assertThat(createdAccount.map(Account::getAccountId).orElseThrow()).isEqualTo(accountId);
    }

    @Test
    void credit_shouldIncreaseAccountBalance() {
        //given
        AccountSnapshot initialAccountState = accountRepository.save(Account.builder()
                                                                            .accountId(UUID.randomUUID())
                                                                            .ownerId(UUID.randomUUID())
                                                                            .build())
                                                               .toSnapshot();
        BigDecimal amount = new BigDecimal("10.05");

        //when
        AccountSnapshot updatedAccount = accountBo.credit(initialAccountState.getAccountId(), amount);
        //then
        assertThat(updatedAccount.getAccountId()).isEqualTo(initialAccountState.getAccountId());
        assertThat(updatedAccount.getBalance()).isGreaterThan(initialAccountState.getBalance())
                                               .isEqualTo(initialAccountState.getBalance().add(amount));
    }

    @Test
    void credit_shouldThrowNotFoundException_whenAccountDoesntExist() {
        //given
        BigDecimal amount = new BigDecimal("10.05");

        //when
        Throwable thrown = catchThrowable(() -> accountBo.credit(UUID.randomUUID(), amount));
        //then
        assertThat(thrown).isNotNull()
                          .isInstanceOf(ResponseStatusException.class);

        assertThat(((ResponseStatusException) thrown).getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void debit_shouldDecreaseAccountBalance() {
        //given
        AccountSnapshot initialAccountState = accountRepository.save(Account.builder()
                                                                            .accountId(UUID.randomUUID())
                                                                            .ownerId(UUID.randomUUID())
                                                                            .build())
                                                               .toSnapshot();
        BigDecimal amount = new BigDecimal("10.05");

        //when
        AccountSnapshot updatedAccount = accountBo.debit(initialAccountState.getAccountId(), amount);
        //then
        assertThat(updatedAccount.getAccountId()).isEqualTo(initialAccountState.getAccountId());
        assertThat(updatedAccount.getBalance()).isLessThan(initialAccountState.getBalance())
                                               .isEqualTo(initialAccountState.getBalance().subtract(amount));
    }

    @Test
    void debit_shouldThrowNotFoundException_whenAccountDoesntExist() {
        //given
        BigDecimal amount = new BigDecimal("10.05");

        //when
        Throwable thrown = catchThrowable(() -> accountBo.debit(UUID.randomUUID(), amount));
        //then
        assertThat(thrown).isNotNull()
                          .isInstanceOf(ResponseStatusException.class);

        assertThat(((ResponseStatusException) thrown).getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}