package com.example.demo.web.client;

import com.example.demo.domain.client.api.ClientBO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
class ClientController {

    private final ClientBO clientBO;

    @PostMapping("/api/clients")
    UUID registerNewClient(@RequestBody NewClient newClient) {
        return clientBO.registerNewClient(newClient.firstName(), newClient.lastName());
    }
}
