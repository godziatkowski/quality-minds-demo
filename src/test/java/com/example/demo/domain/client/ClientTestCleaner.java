package com.example.demo.domain.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientTestCleaner {
    private final ClientRepository clientRepository;

    public void cleanClientTable() {
        clientRepository.deleteAll();
    }
}
