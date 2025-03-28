package com.edacamo.mspersons.interfaces.controllers;

import com.edacamo.mspersons.application.services.RegistrationService;
import com.edacamo.mspersons.interfaces.dto.GetClientByIdRequest;
import com.edacamo.mspersons.interfaces.dto.RegisterRequest;
import com.edacamo.mspersons.interfaces.dto.RegisterResponse;
import com.edacamo.mspersons.interfaces.dto.ResponseGeneric;
import com.edacamo.mspersons.domain.entities.Client;
import com.edacamo.mspersons.infrastructure.exception.ResponseCode;
import com.edacamo.mspersons.application.services.ClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("clientes")
public class ClientController {

    private final ClientService service;
    private final RegistrationService registrationService;

    public ClientController(ClientService service, RegistrationService registrationService) {
        this.service = service;
        this.registrationService = registrationService;
    }

    @GetMapping("")
    public ResponseEntity<ResponseGeneric<List<Client>>> list() {
        List<Client> clients = this.service.findAll();

        ResponseGeneric<List<Client>> response = ResponseGeneric.success(
                HttpStatus.OK.value(),
                ResponseCode.SUCCESS,
                clients
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/id")
    public ResponseEntity<ResponseGeneric<Client>> findById(@RequestBody GetClientByIdRequest request) {
        log.info("Obteniendo el cliente con id {}", request.getClienteId());
        return this.service.findByClienteId(request.getClienteId())
                .map(client -> {
                    ResponseGeneric<Client> response = ResponseGeneric.success(
                            HttpStatus.OK.value(),
                            ResponseCode.SUCCESS,
                            client
                    );
                    return ResponseEntity.ok(response);
                })
                .orElseThrow(() -> new EmptyResultDataAccessException("Cliente con ID " + request.getClienteId() + " no encontrado", 1));
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseGeneric<RegisterResponse>> save(@RequestBody RegisterRequest request) {

       if(request != null) {
           try{
               RegisterResponse register = this.registrationService.registerUser(request);
               ResponseGeneric<RegisterResponse> response = ResponseGeneric.success(
                       HttpStatus.OK.value(),
                       ResponseCode.DATA_CREATED,
                       register
               );

               return ResponseEntity.ok(response);

           } catch (Exception ex) {
               // En caso de error, lanzamos una excepción personalizada para que sea atrapada por el GlobalExceptionHandler
               log.error("Error al registrar el cliente: ", ex);
               throw new RuntimeException("Error al registrar el cliente");  // Aquí puedes lanzar una excepción personalizada si lo prefieres
           }
       } else {
           // Si el cliente es nulo, lanzamos una excepción de validación
           log.error("Cliente nulo recibido");
           throw new IllegalArgumentException("Los datos del cliente no puede ser nulo");
       }
    }

    @PutMapping("/actualizar")
    public ResponseEntity<ResponseGeneric<RegisterResponse>> update(@RequestBody RegisterRequest request) {

        if(request != null) {
            try{
                RegisterResponse register = this.registrationService.updateUser(request);
                ResponseGeneric<RegisterResponse> response = ResponseGeneric.success(
                        HttpStatus.OK.value(),
                        ResponseCode.DATA_UPDATED,
                        register
                );

                return ResponseEntity.ok(response);

            } catch (Exception ex) {
                log.error("Error al actualizar el cliente: ", ex);
                throw new RuntimeException("Error al actualizar el cliente.");
            }
        } else {
            log.error("No se puede actualizar el cliente, por favor intente nuevamente");
            throw new IllegalArgumentException("Los datos del cliente no puede ser nulo.");
        }
    }

    @DeleteMapping("/eliminar/{clienteId}")
    public ResponseEntity<ResponseGeneric<RegisterResponse>> delete(@PathVariable String clienteId) {
        try {
            RegisterResponse responseDelete = this.registrationService.deleteUser(clienteId);
            ResponseGeneric<RegisterResponse> response = ResponseGeneric.success(
                    HttpStatus.OK.value(),
                    ResponseCode.DATA_DELETE,
                    responseDelete
            );
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            log.error("Error al eliminar el cliente: ", ex);
            throw new RuntimeException("Error al eliminar el cliente.");
        }
    }
}
