package com.example.demo.web.client;

import com.example.demo.domain.client.api.ClientFinder;
import com.example.demo.domain.client.api.ClientSnapshot;
import com.example.demo.test.utils.IntegrationTest;
import com.example.demo.test.utils.TestCleaner;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
class ClientControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClientFinder clientFinder;

    @Autowired
    private TestCleaner testCleaner;

    @AfterEach
    void tearDown() {
        testCleaner.cleanDatabase();
    }

    @Test
    void registerNewClient_shouldRegisterNewClient() throws Exception {
        //given
        NewClient newClient = new NewClient("Demo", "User");
        //when
        ResultActions result = mvc.perform(post("/api/clients")
                                               .contentType(MediaType.APPLICATION_JSON)
                                               .accept(MediaType.APPLICATION_JSON)
                                               .content(objectMapper.writeValueAsString(newClient)));
        //then
        String jsonResponse = result.andExpect(status().isOk())
                                    .andExpect(jsonPath("$").exists())
                                    .andReturn()
                                    .getResponse()
                                    .getContentAsString();

        UUID clientId = objectMapper.readValue(jsonResponse, UUID.class);

        ClientSnapshot client = clientFinder.findClient(clientId);
        assertThat(client).isNotNull();
        assertThat(client.getClientId()).isEqualTo(clientId);
    }
}