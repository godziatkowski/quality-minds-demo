package com.example.demo.service.external.client.nbp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class Rate {

    private final String no;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private final LocalDate effectiveDate;

    private final String mid;

    public Rate(@JsonProperty("no") String no,
                @JsonProperty("effectiveDate") LocalDate effectiveDate,
                @JsonProperty("mid") String mid) {
        this.no = no;
        this.effectiveDate = effectiveDate;
        this.mid = mid;
    }
}
