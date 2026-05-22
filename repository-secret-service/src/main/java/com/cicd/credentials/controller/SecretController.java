package com.cicd.credentials.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.cicd.credentials.dto.*;
import com.cicd.credentials.service.SecretService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST Controller for managing authentication secrets.
 * Provides endpoints to secure, retrieve, and delete repository credentials
 */
@RestController
@RequestMapping("/secrets")
public class SecretController {

    private static final Logger log = LoggerFactory.getLogger(SecretController.class);
    private final SecretService secretService;

    /**
     * Constructor injection for the secret management business logic.
     */
    public SecretController(SecretService secretService) {
        this.secretService = secretService;
    }

    /**
     * Endpoint to receive and persist a new authentication secret.
     * * @param secret The secret details sent in the request body.
     * @return The saved SecretEntity with its generated ID.
     * and 201 Created status on successful POST
     */
    @PostMapping
    public ResponseEntity<SecretCreatedResponse> create(@RequestBody SecretCreateRequest request) {
        log.info("Received request to create a new secret with name: {} and provider: {}", request.name(), request.provider());
        SecretCreatedResponse response = secretService.create(request);
        log.info("Successfully created secret with generated ID: {}", response.id());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    /**
     * Endpoint to list all persisted secrets in the system.
     * * @return A list of all stored secrets.
     */
    @GetMapping
    public ResponseEntity<List<SecretDetailsResponse>> getAll() {
        log.info("Received request to fetch all stored secrets");
        List<SecretDetailsResponse> secrets = secretService.findAllDto();
        log.info("Successfully retrieved {} secrets from the system", secrets.size());
        return ResponseEntity.ok(secrets);
    }
    /**
     * Endpoint to delete a secret by its unique identifier.
     * * @param id The ID of the secret to be deleted.
     * @return  ResponseEntity with HTTP 204 No Content status on successful deletion
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        log.info("Received request to delete secret with ID: {}", id);
        try {
            secretService.delete(id);
            log.info("Secret with ID: {} was successfully deleted", id);
            return ResponseEntity.noContent().build();
        } catch (IllegalStateException e) {
            log.error("Failed to delete secret with ID: {}. Error: {}", id, e.getMessage());
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }
    /**
     * Updates an existing secret repository by its unique identifier.
     * Receives the updated configuration fields (name, provider, secretType, secretValue)
     * from the frontend client via HTTP PUT payload.
     *
     * @param id      The database ID of the secret to be updated.
     * @param request The data transfer object (DTO) containing updated secret specifications.
     * @return ResponseEntity containing the updated secret details and HTTP 200 OK status.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SecretCreatedResponse> updateSecret(
            @PathVariable UUID id,
            @RequestBody SecretCreateRequest request
    ) {
        log.info("Received request to update secret with ID: {}. New name: {}, provider: {}", id, request.name(), request.provider());
        SecretCreatedResponse updatedSecret = secretService.update(id, request);
        log.info("Secret with ID: {} was successfully updated", id);
        return ResponseEntity.ok(updatedSecret);
    }
}