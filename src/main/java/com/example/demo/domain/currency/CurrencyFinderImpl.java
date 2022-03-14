package com.example.demo.domain.currency;

import com.example.demo.domain.currency.api.CurrencyFinder;
import com.example.demo.domain.currency.api.CurrencySnapshot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.demo.common.utils.ResponseStatusExceptionGenerator.notFoundException;

@Service
@RequiredArgsConstructor
class CurrencyFinderImpl implements CurrencyFinder {

    private static final String NOT_FOUND_MESSAGE = "Currency with code %s not found.";

    private final CurrencyRepository currencyRepository;

    @Override
    public CurrencySnapshot findCurrencyByCode(String currencyCode) {
        return currencyRepository.findByIsoCode(currencyCode)
                                 .map(Currency::toSnapshot)
                                 .orElseThrow(() -> notFoundException(NOT_FOUND_MESSAGE.formatted(currencyCode)));
    }

}
