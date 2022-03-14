package com.example.demo.web.account;

import java.util.UUID;

public record AccountBalanceChange(UUID accountId, String amount) {
}
