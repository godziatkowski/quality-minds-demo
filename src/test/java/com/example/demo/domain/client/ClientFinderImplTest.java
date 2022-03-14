package com.example.demo.domain.client;

import com.example.demo.domain.client.api.ClientSnapshot;
import com.example.demo.test.utils.IntegrationTest;
import com.example.demo.test.utils.TestCleaner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@IntegrationTest
class ClientFinderImplTest {

    @Autowired
    private ClientFinderImpl clientFinder;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private TestCleaner testCleaner;

    @AfterEach
    void tearDown() {
        testCleaner.cleanDatabase();
    }

    @Test
    void findClient_shouldFindClientByHisId() {
        //given
        Client client = clientRepository.save(Client.builder()
                                                    .clientId(UUID.randomUUID())
                                                    .firstName("demo")
                                                    .lastName("client")
                                                    .build());
        //when
        ClientSnapshot foundClient = clientFinder.findClient(client.getClientId());
        //then
        assertThat(foundClient).isEqualTo(client.toSnapshot());
    }

    @Test
    void findClient_shouldThrowNotFoundException_whenClientNotRegistered() {
        //given
        UUID clientId = UUID.randomUUID();
        //when
        Throwable thrown = catchThrowable(() -> clientFinder.findClient(clientId));
        //then
        assertThat(thrown).isNotNull()
                          .isInstanceOf(ResponseStatusException.class);

        assertThat(((ResponseStatusException) thrown).getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}