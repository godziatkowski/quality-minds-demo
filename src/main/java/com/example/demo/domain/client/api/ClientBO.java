package com.example.demo.domain.client.api;

import java.util.UUID;

public interface ClientBO {

    UUID registerNewClient(String firstName, String lastName);
}
