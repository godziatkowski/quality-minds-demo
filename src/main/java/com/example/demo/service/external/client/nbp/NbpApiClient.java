package com.example.demo.service.external.client.nbp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Slf4j
@Service
@Validated
public class NbpApiClient {

    private final WebClient webClient;

    private final NbpProperties nbpProperties;

    NbpApiClient(NbpProperties nbpProperties, WebClient.Builder webClientBuilder) {
        this.nbpProperties = nbpProperties;
        this.webClient = webClientBuilder.baseUrl(nbpProperties.url())
                                         .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                                         .build();
    }

    public Optional<NbpCurrencyExchangeRate> getTodayExchangeRate(String currencyCode) {
        log.debug("Fetching today exchange rate of <{}> from NBP", currencyCode);
        String endpoint = nbpProperties.exchangeRateToday().formatted(currencyCode);
        return getCurrencyExchangeRate(endpoint);
    }

    public Optional<NbpCurrencyExchangeRate> getLastExchangeRate(String currencyCode, int recordsToLoad) {
        log.debug("Fetching last <{}> exchange rates of <{}> from NBP", recordsToLoad, currencyCode);
        String endpoint = nbpProperties.exchangeRateLast().formatted(currencyCode, recordsToLoad);
        return getCurrencyExchangeRate(endpoint);
    }

    private Optional<NbpCurrencyExchangeRate> getCurrencyExchangeRate(String endpoint) {
        return webClient.get()
                        .uri(endpoint)
                        .retrieve()
                        .bodyToMono(NbpCurrencyExchangeRate.class)
                        .onErrorResume(WebClientResponseException.NotFound.class, notFound -> Mono.empty())
                        .blockOptional();
    }

}
