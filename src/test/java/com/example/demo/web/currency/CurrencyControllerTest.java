package com.example.demo.web.currency;

import com.example.demo.domain.currency.api.CurrencyFinder;
import com.example.demo.domain.currency.api.CurrencySnapshot;
import com.example.demo.test.utils.IntegrationTest;
import com.example.demo.test.utils.TestCleaner;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
class CurrencyControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CurrencyFinder currencyFinder;

    @Autowired
    private TestCleaner testCleaner;

    @AfterEach
    void tearDown() {
        testCleaner.cleanDatabase();
    }

    @Test
    void addCurrencyRestCall_shouldCreateNewCurrency() throws Exception {
        //given
        NewCurrency newCurrency = new NewCurrency("euro", "EUR");
        //when
        ResultActions result = mvc.perform(post("/api/currencies")
                                               .contentType(MediaType.APPLICATION_JSON)
                                               .accept(MediaType.APPLICATION_JSON)
                                               .content(objectMapper.writeValueAsString(newCurrency)));
        //then
        String jsonResponse = result.andExpect(status().isOk())
                                    .andExpect(jsonPath("$").exists())
                                    .andReturn()
                                    .getResponse()
                                    .getContentAsString();

        Long currencyId = objectMapper.readValue(jsonResponse, Long.class);

        CurrencySnapshot currency = currencyFinder.findCurrencyByCode(newCurrency.isoCode());
        assertThat(currency).isNotNull();
        assertThat(currency.getId()).isEqualTo(currencyId);
    }

    @ParameterizedTest
    @ValueSource(strings = { "DEMO", "DE", "" })
    void addCurrencyRestCall_shouldReturnBadRequest_onInvalidIsoCode(String isoCode) throws Exception {
        //given
        NewCurrency newCurrency = new NewCurrency("euro", isoCode);
        //when
        ResultActions result = mvc.perform(post("/api/currencies")
                                               .contentType(MediaType.APPLICATION_JSON)
                                               .accept(MediaType.APPLICATION_JSON)
                                               .content(objectMapper.writeValueAsString(newCurrency)));
        //then
        result.andExpect(status().isBadRequest());
    }

}