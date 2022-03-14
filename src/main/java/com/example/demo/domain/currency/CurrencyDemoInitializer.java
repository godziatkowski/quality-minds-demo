package com.example.demo.domain.currency;

import com.example.demo.initializer.DemoInitializer;
import com.example.demo.initializer.InitializerProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("demo")
@Component
@RequiredArgsConstructor
public class CurrencyDemoInitializer implements DemoInitializer {

    private final CurrencyRepository currencyRepository;

    private final InitializerProperties initializerProperties;

    @Override
    public void init() {
        Currency currency = Currency.builder()
                                    .name(initializerProperties.currencyName())
                                    .isoCode(initializerProperties.currencyIsoCode())
                                    .build();
        currencyRepository.save(currency);
    }
}
