package com.example.demo.service.external.client.nbp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static com.example.demo.service.external.client.nbp.NbpApiResponseGenerator.sampleCurrencyExchangeRate;
import static com.example.demo.service.external.client.nbp.NbpApiResponseGenerator.sampleRate;
import static org.assertj.core.api.Assertions.assertThat;

class NbpApiClientTest {

    private static final String ENDPOINT_FOR_TODAY_EXCHANGE_RATE = "/api/exchangerates/rates/A/%s/today/";

    private static final String ENDPOINT_FOR_LAST_EXCHANGE_RATE = "/api/exchangerates/rates/A/%s/last/%d";

    private ObjectMapper objectMapper;

    private NbpApiClient nbpApiClient;

    private MockWebServer server;

    private NbpProperties nbpProperties;

    @BeforeEach
    void initialize() throws IOException {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        server = new MockWebServer();
        server.start();
        String rootUrl = server.url("").toString();

        nbpProperties = new NbpProperties(rootUrl,
                                          ENDPOINT_FOR_TODAY_EXCHANGE_RATE,
                                          ENDPOINT_FOR_LAST_EXCHANGE_RATE);

        nbpApiClient = new NbpApiClient(nbpProperties, WebClient.builder());
    }

    @AfterEach
    void tearDown() throws IOException {
        server.shutdown();
    }

    @Test
    void getTodayExchangeRate_shouldReturnCorrectValueOnOk() throws InterruptedException, JsonProcessingException {
        //given
        String currencyCode = "EUR";
        String endpoint = nbpProperties.exchangeRateToday()
                                       .formatted(currencyCode);

        Rate rate = sampleRate(4.7924d).build();
        NbpCurrencyExchangeRate nbpCurrencyExchangeRate = sampleCurrencyExchangeRate(List.of(rate)).build();

        MockResponse response = new MockResponse()
            .addHeader("Content-Type", "application/json")
            .setBody(objectMapper.writeValueAsString(nbpCurrencyExchangeRate));
        server.enqueue(response);

        //when
        Optional<NbpCurrencyExchangeRate> optionalExchangeRate = nbpApiClient.getTodayExchangeRate(currencyCode);

        //then
        assertThat(optionalExchangeRate).isPresent();
        NbpCurrencyExchangeRate result = optionalExchangeRate.orElseThrow();
        assertThat(result).isEqualTo(nbpCurrencyExchangeRate);

        RecordedRequest request = server.takeRequest();
        assertThat(request.getMethod()).isEqualTo("GET");
        assertThat(request.getPath()).isEqualTo(endpoint);
    }

    @Test
    void getTodayExchangeRate_shouldReturnOptionalEmptyOnNotFound() throws InterruptedException {
        //given
        String currencyCode = "EUR";
        String endpoint = nbpProperties.exchangeRateToday()
                                       .formatted(currencyCode);

        MockResponse response = new MockResponse()
            .addHeader("Content-Type", "application/json")
            .setResponseCode(404)
            .setBody("404 NotFound - Not Found - Brak danych");
        server.enqueue(response);

        //when
        Optional<NbpCurrencyExchangeRate> optionalExchangeRate = nbpApiClient.getTodayExchangeRate(currencyCode);

        //then
        assertThat(optionalExchangeRate).isEmpty();

        RecordedRequest request = server.takeRequest();
        assertThat(request.getMethod()).isEqualTo("GET");
        assertThat(request.getPath()).isEqualTo(endpoint);
    }

    @Test
    void getCurrentExchangeRate_shouldReturnCorrectValueOk() throws InterruptedException, JsonProcessingException {
        //given
        String currencyCode = "EUR";
        int ratesCount = 5;
        String endpoint = nbpProperties.exchangeRateLast().formatted(currencyCode, ratesCount);

        List<Rate> rates = IntStream.range(0, ratesCount)
                                    .mapToObj(i -> sampleRate(i + 0.0001d).build())
                                    .toList();
        NbpCurrencyExchangeRate nbpCurrencyExchangeRate = sampleCurrencyExchangeRate(rates).build();

        MockResponse response = new MockResponse()
            .addHeader("Content-Type", "application/json")
            .setBody(objectMapper.writeValueAsString(nbpCurrencyExchangeRate));
        server.enqueue(response);

        //when
        Optional<NbpCurrencyExchangeRate> optionalExchangeRate
            = nbpApiClient.getLastExchangeRate(currencyCode, ratesCount);

        //then
        assertThat(optionalExchangeRate).isPresent();
        NbpCurrencyExchangeRate result = optionalExchangeRate.orElseThrow();
        assertThat(result).isEqualTo(nbpCurrencyExchangeRate);
        assertThat(result.getRates()).hasSize(ratesCount);

        RecordedRequest request = server.takeRequest();
        assertThat(request.getMethod()).isEqualTo("GET");
        assertThat(request.getPath()).isEqualTo(endpoint);
    }

    @Test
    void getCurrentExchangeRate_shouldReturnOptionalEmptyOnNotFound() throws InterruptedException {
        //given
        String currencyCode = "EUR";
        int ratesCount = 5;
        String endpoint = nbpProperties.exchangeRateLast().formatted(currencyCode, ratesCount);

        MockResponse response = new MockResponse()
            .addHeader("Content-Type", "application/json")
            .setResponseCode(404)
            .setBody("404 NotFound - Not Found - Brak danych");
        server.enqueue(response);

        //when
        Optional<NbpCurrencyExchangeRate> optionalExchangeRate
            = nbpApiClient.getLastExchangeRate(currencyCode, ratesCount);

        //then
        assertThat(optionalExchangeRate).isEmpty();

        RecordedRequest request = server.takeRequest();
        assertThat(request.getMethod()).isEqualTo("GET");
        assertThat(request.getPath()).isEqualTo(endpoint);
    }
}