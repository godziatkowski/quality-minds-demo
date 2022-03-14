package com.example.demo.domain.client;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
interface ClientRepository extends CrudRepository<Client, UUID> {
}
