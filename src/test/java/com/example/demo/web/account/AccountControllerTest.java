package com.example.demo.web.account;

import com.example.demo.domain.account.api.AccountBO;
import com.example.demo.domain.account.api.AccountFinder;
import com.example.demo.domain.account.api.AccountSnapshot;
import com.example.demo.domain.client.api.ClientBO;
import com.example.demo.domain.currency.api.CurrencyBO;
import com.example.demo.service.external.client.nbp.NbpApiClient;
import com.example.demo.service.external.client.nbp.NbpCurrencyExchangeRate;
import com.example.demo.service.external.client.nbp.Rate;
import com.example.demo.test.utils.IntegrationTest;
import com.example.demo.test.utils.TestCleaner;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.example.demo.common.utils.DefaultCurrencyScale.DEFAULT_ROUNDING_MODE;
import static com.example.demo.service.external.client.nbp.NbpApiResponseGenerator.sampleCurrencyExchangeRate;
import static com.example.demo.service.external.client.nbp.NbpApiResponseGenerator.sampleRate;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
class AccountControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AccountFinder accountFinder;

    @Autowired
    private ClientBO clientBO;

    @Autowired
    private AccountBO accountBO;

    @Autowired
    private CurrencyBO currencyBO;

    @MockBean
    private NbpApiClient nbpApiClient;

    @Autowired
    private TestCleaner testCleaner;

    private UUID clientId;

    private UUID existingAccountId;

    @BeforeEach
    void setup() {
        clientId = clientBO.registerNewClient("Demo", "Client");
        existingAccountId = accountBO.openAccountForUser(clientId);
    }

    @AfterEach
    void tearDown() {
        testCleaner.cleanDatabase();
    }

    @Test
    void openAccountForClientRestCall_shouldCreateNewBankAccount() throws Exception {
        //given
        NewAccount newAccount = new NewAccount(clientId);
        //when
        ResultActions result = mvc.perform(post("/api/accounts")
                                               .accept(MediaType.APPLICATION_JSON)
                                               .contentType(MediaType.APPLICATION_JSON)
                                               .content(objectMapper.writeValueAsString(newAccount)));
        //then
        String jsonResponse = result.andExpect(status().isOk())
                                    .andExpect(jsonPath("$").exists())
                                    .andReturn()
                                    .getResponse()
                                    .getContentAsString();

        UUID newAccountId = objectMapper.readValue(jsonResponse, UUID.class);

        AccountSnapshot account = accountFinder.getAccount(newAccountId);
        assertThat(account).isNotNull();
        assertThat(account.getAccountId()).isEqualTo(newAccountId);
    }

    @Test
    void grantCreditToAccountRestCall_shouldIncreaseAccountBalance() throws Exception {
        //given
        BigDecimal originalBalance = accountFinder.getAccount(existingAccountId).getBalance();
        String valueToAdd = "45.251";
        AccountBalanceChange accountBalanceChange = new AccountBalanceChange(existingAccountId, valueToAdd);

        //when
        ResultActions result = mvc.perform(put("/api/accounts/credit")
                                               .accept(MediaType.APPLICATION_JSON)
                                               .contentType(MediaType.APPLICATION_JSON)
                                               .content(objectMapper.writeValueAsString(accountBalanceChange)));

        //then
        result.andExpect(status().isOk())
              .andExpect(jsonPath("$.ownerId").value(clientId.toString()))
              .andExpect(jsonPath("$.accountId").value(existingAccountId.toString()))
              .andExpect(jsonPath("$.balance").value(greaterThan(originalBalance.doubleValue())));
    }

    @Test
    void chargeAccountRestCall_shouldDecreaseAccountBalance() throws Exception {
        //given
        accountBO.credit(existingAccountId, new BigDecimal("45.251"));
        BigDecimal originalBalance = accountFinder.getAccount(existingAccountId).getBalance();
        String valueToSubtract = "35.0123";
        AccountBalanceChange accountBalanceChange = new AccountBalanceChange(existingAccountId, valueToSubtract);

        //when
        ResultActions result = mvc.perform(put("/api/accounts/debit")
                                               .accept(MediaType.APPLICATION_JSON)
                                               .contentType(MediaType.APPLICATION_JSON)
                                               .content(objectMapper.writeValueAsString(accountBalanceChange)));

        //then
        result.andExpect(status().isOk())
              .andExpect(jsonPath("$.ownerId").value(clientId.toString()))
              .andExpect(jsonPath("$.accountId").value(existingAccountId.toString()))
              .andExpect(jsonPath("$.balance").value(lessThan(originalBalance.doubleValue())));
    }

    @Test
    void getAccountBalanceRestCall_withoutCurrencyCode_shouldReturnAccountBalance() throws Exception {
        //given
        String url = "/api/accounts/%s/balance".formatted(existingAccountId);
        BigDecimal balance = accountBO.credit(existingAccountId, new BigDecimal("100.0")).getBalance();
        //when
        ResultActions result = mvc.perform(get(url)
                                               .accept(MediaType.APPLICATION_JSON));
        //then
        result.andExpect(status().isOk())
              .andExpect(jsonPath("$.currency").value("polski złoty"))
              .andExpect(jsonPath("$.code").value("PLN"))
              .andExpect(jsonPath("$.value").value(balance.doubleValue()));
    }

    @Test
    void getAccountBalance_withCurrencyCode_shouldReturnAccountBalanceInCurrency() throws Exception {
        //given
        String url = "/api/accounts/%s/balance".formatted(existingAccountId);
        String currencyCode = "DEM";
        String currencyName = "demo";
        currencyBO.supportCurrency(currencyName, currencyCode);

        BigDecimal exchangeRate = new BigDecimal("10.0");
        mockExternalApiResponse(currencyCode, currencyName, exchangeRate);

        BigDecimal balance = accountBO.credit(existingAccountId, new BigDecimal("100.0")).getBalance();

        BigDecimal balanceInCurrency = balance.divide(exchangeRate, DEFAULT_ROUNDING_MODE);

        //when
        ResultActions result = mvc.perform(get(url)
                                               .param("currency", currencyCode)
                                               .accept(MediaType.APPLICATION_JSON));
        //then
        result.andExpect(status().isOk())
              .andExpect(jsonPath("$.currency").value(currencyName))
              .andExpect(jsonPath("$.code").value(currencyCode))
              .andExpect(jsonPath("$.value").value(balanceInCurrency.doubleValue()));
    }

    private void mockExternalApiResponse(String currencyCode, String currencyName, BigDecimal exchangeRate) {
        Rate rate = sampleRate(exchangeRate.doubleValue()).build();
        NbpCurrencyExchangeRate nbpCurrencyExchangeRate = sampleCurrencyExchangeRate(List.of(rate))
            .code(currencyCode)
            .currency(currencyName)
            .build();
        given(nbpApiClient.getTodayExchangeRate(currencyCode)).willReturn(Optional.of(nbpCurrencyExchangeRate));
    }
}