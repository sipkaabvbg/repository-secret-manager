package com.cicd.credentials.service;

import com.cicd.credentials.dto.RepoDetailsResponse;
import com.cicd.credentials.entity.ExternalRepoEntity;
import com.cicd.credentials.entity.SecretEntity;
import com.cicd.credentials.repository.ExternalRepoRepository;
import com.cicd.credentials.repository.SecretRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

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
     * Registers a new repository. Supports both Private (with secretId)
     * and Public (secretId is null) repositories.
     * @param url      the URL of the code repository
     * @param secretId the unique identifier of the authentication secret
     * @return the saved ExternalRepoEntity
     * @throws RuntimeException if the secret with the given ID does not exist
     */
    public ExternalRepoEntity create(String name,String url, Long secretId) {
        ExternalRepoEntity repo = new ExternalRepoEntity();
        repo.setUrl(url);
        repo.setName(name);
        if (secretId != null) {
            SecretEntity secret = secretRepository.findById(secretId)
                    .orElseThrow(() -> new RuntimeException("Secret not found"));
            repo.setSecret(secret);
        } else {
            repo.setSecret(null);
        }
        return externalRepoRepository.save(repo);
    }

    /**
     * Retrieves a list of all External Repositories from the database.
     * @return A list of all entities of type ExternalRepoEntity.
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