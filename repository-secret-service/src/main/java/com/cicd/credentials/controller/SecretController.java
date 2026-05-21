package com.cicd.credentials.controller;

import com.cicd.credentials.dto.SecretCreateRequest;
import com.cicd.credentials.dto.SecretCreatedResponse;
import com.cicd.credentials.entity.SecretEntity;
import com.cicd.credentials.service.SecretService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public List<SecretEntity> getAll() {
        return secretService.findAll();
    }

    /**
     * Endpoint to delete a secret by its unique identifier.
     * * @param id The ID of the secret to be deleted.
     * @return  ResponseEntity with HTTP 204 No Content status on successful deletion
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        secretService.delete(id);
        return ResponseEntity.noContent().build();
    }
}