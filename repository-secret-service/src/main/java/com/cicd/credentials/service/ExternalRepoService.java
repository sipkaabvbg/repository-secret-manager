package com.cicd.credentials.service;

import com.cicd.credentials.entity.ExternalRepoEntity;
import com.cicd.credentials.entity.SecretEntity;
import com.cicd.credentials.repository.ExternalRepoRepository;
import com.cicd.credentials.repository.SecretRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class handling the business logic for managing code repositories
 * and their associated authentication secrets.
 */
@Service
public class ExternalRepoService {

    private final ExternalRepoRepository externalRepoRepository;
    private final SecretRepository secretRepository;

    /**
     * Constructor injection for the required database repositories.
     *
     * @param externalRepoRepository the repository for CRUD operations on ExternalRepoEntity
     * @param secretRepository     the repository for CRUD operations on SecretEntity
     */
    public ExternalRepoService(ExternalRepoRepository externalRepoRepository,
                               SecretRepository secretRepository) {
        this.externalRepoRepository = externalRepoRepository;
        this.secretRepository = secretRepository;
    }

    /**
     * Creates and persists a new repository linked to an existing secret.
     *
     * @param url      the URL of the code repository
     * @param secretId the unique identifier of the authentication secret
     * @return the saved ExternalRepoEntity
     * @throws RuntimeException if the secret with the given ID does not exist
     */
    public ExternalRepoEntity create(String url, Long secretId) {
        SecretEntity secret = secretRepository.findById(secretId)
                .orElseThrow(() -> new RuntimeException("Secret not found"));

        ExternalRepoEntity repo = new ExternalRepoEntity();
        repo.setUrl(url);
        repo.setSecret(secret);

        return externalRepoRepository.save(repo);
    }

    /**
     * Retrieves all repositories from the database.
     *
     * @return a list of all registered ExternalRepoEntity objects
     */
    public List<ExternalRepoEntity> findAll() {
        return externalRepoRepository.findAll();
    }

    /**
     * Deletes a repository by its unique identifier.
     *
     * @param id the unique identifier of the repository to be deleted
     */
    public void delete(Long id) {
        externalRepoRepository.deleteById(id);
    }
}