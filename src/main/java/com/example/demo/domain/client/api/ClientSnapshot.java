package com.example.demo.domain.client.api;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
@EqualsAndHashCode
public class ClientSnapshot {

    private final UUID clientId;

    private final String firstName;

    private final String lastName;
}
