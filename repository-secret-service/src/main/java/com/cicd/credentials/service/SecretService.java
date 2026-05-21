package com.cicd.credentials.service;

import com.cicd.credentials.dto.SecretCreateRequest;
import com.cicd.credentials.dto.SecretCreatedResponse;
import com.cicd.credentials.entity.SecretEntity;
import com.cicd.credentials.repository.SecretRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class handling the business logic for managing authentication secrets
 */
@Service
public class SecretService {

    private final SecretRepository secretRepository;

    /**
     * Constructor injection for the required database repository.
     * @param secretRepository the repository for CRUD operations on SecretEntity
     */
    public SecretService(SecretRepository secretRepository) {
        this.secretRepository = secretRepository;
    }

    /**
     * Persists a new authentication secret in the database.
     *
     * @param request the secret DTO containing the credentials data
     * @return the saved SecretEntity including its generated database ID
     */
    public SecretCreatedResponse create(SecretCreateRequest request) {
        SecretEntity secretEntity = new SecretEntity(
                request.name(),
                request.secretValue(),
                request.provider(),
                request.secretType()
        );

        SecretEntity savedSecret = secretRepository.save(secretEntity);
        return new SecretCreatedResponse(savedSecret.getId());
    }

    /**
     * Retrieves all authentication secrets from the database.
     * @return a list of all registered SecretEntity objects
     */
    public List<SecretEntity> findAll() {
        return secretRepository.findAll();
    }

    /**
     * Deletes an authentication secret by its unique identifier.
     * @param id the unique identifier of the secret to be deleted
     */
    public void delete(Long id) {
        secretRepository.deleteById(id);
    }
}