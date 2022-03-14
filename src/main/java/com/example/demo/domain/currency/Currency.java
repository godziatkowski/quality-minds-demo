package com.example.demo.domain.currency;

import com.example.demo.domain.currency.api.CurrencySnapshot;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @Size(min = 3,
          max = 3)
    private String isoCode;

    @NotNull
    private String name;

    CurrencySnapshot toSnapshot() {
        return CurrencySnapshot.builder()
                               .id(id)
                               .isoCode(isoCode)
                               .name(name)
                               .build();
    }

}
