package com.edacamo.mspersons.application.services;

import com.edacamo.mspersons.application.event.ClientEvent;
import com.edacamo.mspersons.domain.entities.Client;
import com.edacamo.mspersons.domain.repositories.ClientRepository;
import com.edacamo.mspersons.interfaces.dto.RegisterRequest;
import com.edacamo.mspersons.interfaces.dto.RegisterResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RegistrationServiceImpl implements RegistrationService {

    final private ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    private final KafkaTemplate<String, ClientEvent> kafkaTemplate;

    public RegistrationServiceImpl(ClientRepository clientRepository, PasswordEncoder passwordEncoder, KafkaTemplate<String, ClientEvent> kafkaTemplate) {
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
        this.kafkaTemplate = kafkaTemplate;
    }

    public RegisterResponse registerUser(RegisterRequest request) {

        if (clientRepository.findByClienteId(request.getUsuario()) != null) {
            return new RegisterResponse(String.format("El usuario %s ya existe.", request.getUsuario()));
        }

        Client cliente = new Client();
        cliente.setClienteId(request.getUsuario());
        cliente.setContrasenia(passwordEncoder.encode(request.getPassword()));
        cliente.setEstado(true);

        cliente.setNombre(request.getNombre());
        cliente.setGenero(request.getGenero());
        cliente.setEdad(request.getEdad());
        cliente.setIdentificacion(request.getIdentificacion());
        cliente.setDireccion(request.getDireccion());
        cliente.setTelefono(request.getTelefono());

        clientRepository.save(cliente);
        this.publishClientCreated(cliente); //Produce el mensaje Kafka
        return new RegisterResponse("Usuario registrado correctamente");
    }

    // Método para publicar el evento al crear el cliente
    public void publishClientCreated(Client client) {
        ClientEvent event = new ClientEvent(
                client.getClienteId(),
                client.getNombre(),
                client.getGenero(),
                client.getEdad(),
                client.getIdentificacion(),
                client.getDireccion(),
                client.getTelefono(),
                client.getEstado()
        );
        
        log.info("Publicando evento de cliente creado: {}", event);
        kafkaTemplate.send("client-events", event.getClienteId(), event);
    }
}
