package com.example.demo.domain.client;

import com.example.demo.domain.client.api.ClientSnapshot;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
class Client {

    @Id
    private UUID clientId;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    ClientSnapshot toSnapshot() {
        return ClientSnapshot.builder()
                             .clientId(clientId)
                             .firstName(firstName)
                             .lastName(lastName)
                             .build();
    }
}
