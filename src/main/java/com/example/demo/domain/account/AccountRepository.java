package com.example.demo.domain.account;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

interface AccountRepository extends CrudRepository<Account, UUID> {

    List<Account> findAllByOwnerId(UUID ownerId);

    Optional<Account> findByOwnerIdAndAccountId(UUID ownerId, UUID accountId);
}
