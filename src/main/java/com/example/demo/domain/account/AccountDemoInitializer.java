package com.example.demo.domain.account;

import com.example.demo.initializer.DemoInitializer;
import com.example.demo.initializer.InitializerProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Profile("demo")
@Component
@RequiredArgsConstructor
public class AccountDemoInitializer implements DemoInitializer {

    private final AccountRepository accountRepository;

    private final InitializerProperties initializerProperties;

    @Override
    public void init() {
        Account account = Account.builder()
                               .accountId(initializerProperties.accountId())
                               .ownerId(initializerProperties.clientId())
                               .build();
        account.credit(new BigDecimal(initializerProperties.accountBalance()));
        accountRepository.save(account);
    }
}
