package com.example.demo.domain.client.api;

import java.util.UUID;

public interface ClientFinder {

    ClientSnapshot findClient(UUID clientId);
}
