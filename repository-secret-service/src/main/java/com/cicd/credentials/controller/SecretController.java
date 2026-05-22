package com.cicd.credentials.controller;

import com.cicd.credentials.dto.*;
import com.cicd.credentials.entity.SecretEntity;
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
        return ResponseEntity.status(HttpStatus.CREATED).body(secretService.create(request));
    }
    /**
     * Endpoint to list all persisted secrets in the system.
     * * @return A list of all stored secrets.
     */
    @GetMapping
    public List<SecretDetailsResponse> getAll() {
        List<SecretEntity> secretEntities = secretService.findAll();
        return secretEntities.stream()
                .map(entity -> new SecretDetailsResponse(
                        entity.getId(),
                        entity.getName(),
                        entity.getProvider(),
                        false // Forces 'editable' to be false initially so SAPUI5 renders read-only texts
                ))
                .toList();
    }

    /**
     * Endpoint to delete a secret by its unique identifier.
     * * @param id The ID of the secret to be deleted.
     * @return  ResponseEntity with HTTP 204 No Content status on successful deletion
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        try {
            secretService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalStateException e) {
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
        SecretCreatedResponse updatedRepo =  secretService.update(id, request);
        return ResponseEntity.ok(updatedRepo);
    }
}