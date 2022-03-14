package com.example.demo.service.external.client.nbp;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;


@ConstructorBinding
@ConfigurationProperties(prefix = "nbp")
public record NbpProperties(String url, String exchangeRateToday, String exchangeRateLast) {
}
