package com.example.demo.web.currency;

import com.example.demo.domain.currency.api.CurrencyBO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
class CurrencyController {

    private final CurrencyBO currencyBO;


    @PostMapping("/api/currencies")
    Long addCurrency(@Valid @RequestBody NewCurrency newCurrency) {
        return currencyBO.supportCurrency(newCurrency.name(), newCurrency.isoCode());
    }

}
