package com.example.demo.initializer;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.context.annotation.Profile;

import java.util.UUID;

@Profile("demo")
@ConstructorBinding
@ConfigurationProperties(prefix = "demo")
public record InitializerProperties(UUID clientId,
                                    String clientFirstName,
                                    String clientLastName,
                                    UUID accountId,
                                    String accountBalance,
                                    String currencyName,
                                    String currencyIsoCode
) {
}
