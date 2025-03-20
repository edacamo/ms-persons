package com.edacamo.mspersons.application.services;

import com.edacamo.mspersons.domain.entities.Client;


import java.util.List;
import java.util.Optional;

public interface ClientService  {

    List<Client> findAll();

    Optional<Client> findById(Long id);

    void deleteById(Long id);
}
