package com.cicd.credentials.service;

import com.cicd.credentials.dto.*;
import com.cicd.credentials.entity.ExternalRepoEntity;
import com.cicd.credentials.entity.SecretEntity;
import com.cicd.credentials.repository.ExternalRepoRepository;
import com.cicd.credentials.repository.SecretRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service class handling the business logic for managing authentication secrets
 */
@Service
public class SecretService {

    private final SecretRepository secretRepository;
    private final ExternalRepoRepository externalRepoRepository;

    /**
     * Constructor injection for the required database repository.
     * @param secretRepository the repository for CRUD operations on SecretEntity
     */
    public SecretService(SecretRepository secretRepository, ExternalRepoRepository externalRepoRepository) {
        this.secretRepository = secretRepository;
        this.externalRepoRepository = externalRepoRepository;
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
        return new SecretCreatedResponse(savedSecret.getId(),false);
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
     * Throws an exception if the secret is currently linked to any repository.
     * * @param id the unique identifier of the secret to be deleted
     * @throws IllegalStateException if the secret is in use by a repository
     */
    @Transactional
    public void delete(Long id) {
        if (!secretRepository.existsById(id)) {
            throw new RuntimeException("Secret not found with id: " + id);
        }
        boolean isSecretInUse = externalRepoRepository.existsBySecretId(id);
        if (isSecretInUse) {
            throw new IllegalStateException("Cannot delete this secret because it is linked to repository.");
        }
        secretRepository.deleteById(id);
    }

    /**
     * Updates an existing secret entity in the database with new values.
     * * @param id      The unique identifier of the secret to change.
     * @param request The DTO containing the updated fields (name, value, provider, type).
     * @return SecretCreatedResponse containing the modified entity's ID.
     * @throws RuntimeException if no secret is found with the given ID.
     */
    @Transactional
    public SecretCreatedResponse update(Long id, SecretCreateRequest request) {
        SecretEntity secret = secretRepository.findById(id )
                .orElseThrow(() -> new RuntimeException("Repository not found with id: " + id));

        secret.setName(request.name());
        secret.setSecretValue(request.secretValue());
        secret.setProvider(request.provider());
        secret.setSecretType(request.secretType());
        SecretEntity savedRepo = secretRepository.save(secret);

        return new SecretCreatedResponse(
                savedRepo.getId(),
                false
        );
    }
}