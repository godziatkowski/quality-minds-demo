package com.example.demo.domain.client;

import com.example.demo.test.utils.IntegrationTest;
import com.example.demo.test.utils.TestCleaner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
class ClientBOImptTest {

    @Autowired
    private ClientBOImpt clientBO;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private TestCleaner testCleaner;

    @AfterEach
    void tearDown() {
        testCleaner.cleanDatabase();
    }

    @Test
    void registerNewClient_shouldCreateNewClientEntity() {
        //given
        String firstName = "demo";
        String lastName = "client";
        //when
        UUID clientId = clientBO.registerNewClient(firstName, lastName);
        //then
        assertThat(clientId).isNotNull();
        Optional<Client> createdClient = clientRepository.findById(clientId);
        assertThat(createdClient).isPresent();
        assertThat(createdClient.map(Client::getFirstName).orElseThrow()).isEqualTo(firstName);
        assertThat(createdClient.map(Client::getLastName).orElseThrow()).isEqualTo(lastName);
    }
}