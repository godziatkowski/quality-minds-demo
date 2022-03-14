package com.example.demo.domain.currency;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface CurrencyRepository extends CrudRepository<Currency, Long> {

    Optional<Currency> findByIsoCode(String isoCode);
}
