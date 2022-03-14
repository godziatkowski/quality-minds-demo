package com.example.demo.domain.client;

import com.example.demo.domain.client.api.ClientFinder;
import com.example.demo.domain.client.api.ClientSnapshot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.example.demo.common.utils.ResponseStatusExceptionGenerator.notFoundException;

@Service
@RequiredArgsConstructor
class ClientFinderImpl implements ClientFinder {

    private static final String NOT_FOUND_MESSAGE = "Client with id %s not found";

    private final ClientRepository clientRepository;

    public ClientSnapshot findClient(UUID clientId) {
        return clientRepository.findById(clientId)
                               .map(Client::toSnapshot)
                               .orElseThrow(() -> notFoundException(NOT_FOUND_MESSAGE.formatted(clientId)));
    }

}
