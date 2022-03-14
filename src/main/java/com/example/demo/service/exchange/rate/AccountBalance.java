package com.example.demo.service.exchange.rate;

import java.math.BigDecimal;

public record AccountBalance(String currency, String code, BigDecimal value) {}
