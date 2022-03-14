package com.example.demo.domain.currency;

import com.example.demo.domain.currency.api.CurrencyBO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
class CurrencyBOImpl implements CurrencyBO {

    private final CurrencyRepository currencyRepository;

    public Long supportCurrency(String name, String isoCode) {
        Currency currency = Currency.builder()
                                    .name(name)
                                    .isoCode(isoCode)
                                    .build();
        currency = currencyRepository.save(currency);
        return currency.getId();
    }
}
