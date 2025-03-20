package com.edacamo.mspersons.application.services;

import com.edacamo.mspersons.application.events.ClientEvent;
import com.edacamo.mspersons.domain.entities.Client;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PublishClientCreatedEvent {

    private final KafkaTemplate<String, ClientEvent> kafkaTemplate;

    public PublishClientCreatedEvent(KafkaTemplate<String, ClientEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
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
