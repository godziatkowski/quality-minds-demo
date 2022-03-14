package com.example.demo.domain.client;

import com.example.demo.domain.client.api.ClientBO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
class ClientBOImpt implements ClientBO {

    private final ClientRepository clientRepository;

    @Override
    public UUID registerNewClient(String firstName, String lastName) {
        Client newClient = Client.builder()
                                 .clientId(UUID.randomUUID())
                                 .firstName(firstName)
                                 .lastName(lastName)
                                 .build();
        newClient = clientRepository.save(newClient);
        return newClient.toSnapshot().getClientId();
    }
}
