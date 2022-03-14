package com.example.demo.web.account;

import com.example.demo.domain.account.api.AccountBO;
import com.example.demo.domain.account.api.AccountFinder;
import com.example.demo.domain.account.api.AccountSnapshot;
import com.example.demo.domain.client.api.ClientFinder;
import com.example.demo.domain.client.api.ClientSnapshot;
import com.example.demo.domain.currency.api.CurrencyFinder;
import com.example.demo.domain.currency.api.CurrencySnapshot;
import com.example.demo.service.exchange.rate.AccountBalance;
import com.example.demo.service.exchange.rate.ExchangeCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static com.example.demo.common.utils.DefaultCurrencyScale.useDefaultScaleAndRounding;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountBO accountBO;

    private final ClientFinder clientFinder;

    private final AccountFinder accountFinder;

    private final CurrencyFinder currencyFinder;

    private final ExchangeCalculator exchangeCalculator;

    @PostMapping("/api/accounts")
    UUID openAccountForClient(@RequestBody NewAccount newAccount) {
        ClientSnapshot client = clientFinder.findClient(newAccount.clientId());
        return accountBO.openAccountForUser(client.getClientId());
    }

    @PutMapping("/api/accounts/credit")
    AccountSnapshot grantCreditToAccount(@RequestBody AccountBalanceChange accountBalanceChange) {
        AccountSnapshot accountSnapshot = accountFinder.getAccount(accountBalanceChange.accountId());
        BigDecimal amount = new BigDecimal(accountBalanceChange.amount());
        amount = useDefaultScaleAndRounding(amount);
        return accountBO.credit(accountSnapshot.getAccountId(), amount);

    }

    @PutMapping("/api/accounts/debit")
    AccountSnapshot chargeAccount(@RequestBody AccountBalanceChange accountBalanceChange) {
        AccountSnapshot accountSnapshot = accountFinder.getAccount(accountBalanceChange.accountId());
        BigDecimal amount = new BigDecimal(accountBalanceChange.amount());
        amount = useDefaultScaleAndRounding(amount);
        return accountBO.debit(accountSnapshot.getAccountId(), amount);
    }

    @GetMapping("/api/accounts/{accountId}/balance")
    AccountBalance getAccountBalance(@PathVariable UUID accountId,
                                     @RequestParam(name = "currency",
                                                   required = false) Optional<String> optionalCurrencyCode) {
        AccountSnapshot clientAccount = accountFinder.getAccount(accountId);
        return optionalCurrencyCode.map(currencyCode -> getAccountBalanceInCurrency(clientAccount, currencyCode))
                                   .orElse(getAccountBalanceInPln(clientAccount));
    }

    private AccountBalance getAccountBalanceInCurrency(AccountSnapshot account, String currencyCode) {
        CurrencySnapshot currency = currencyFinder.findCurrencyByCode(currencyCode);
        BigDecimal exchangedValue = exchangeCalculator.calculateExchangedValue(currency, account.getBalance());
        return new AccountBalance(currency.getName(), currency.getIsoCode(), exchangedValue);
    }

    private AccountBalance getAccountBalanceInPln(AccountSnapshot account) {
        return new AccountBalance("polski złoty", "PLN", account.getBalance());
    }
}
