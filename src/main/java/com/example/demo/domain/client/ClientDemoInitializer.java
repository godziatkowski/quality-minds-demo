package com.example.demo.domain.client;

import com.example.demo.initializer.DemoInitializer;
import com.example.demo.initializer.InitializerProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("demo")
@Component
@RequiredArgsConstructor
public class ClientDemoInitializer implements DemoInitializer {

    private final ClientRepository clientRepository;

    private final InitializerProperties initializerProperties;

    @Override
    public void init() {
        Client client = Client.builder()
                              .clientId(initializerProperties.clientId())
                              .firstName(initializerProperties.clientFirstName())
                              .lastName(initializerProperties.clientLastName())
                              .build();
        clientRepository.save(client);
    }
}
