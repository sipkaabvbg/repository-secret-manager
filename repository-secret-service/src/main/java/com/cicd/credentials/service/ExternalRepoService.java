package com.cicd.credentials.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.cicd.credentials.dto.CreateRepoIdResponse;
import com.cicd.credentials.dto.RepoDetailsResponse;
import com.cicd.credentials.dto.UpdateRepoRequest;
import com.cicd.credentials.entity.ExternalRepoEntity;
import com.cicd.credentials.entity.SecretEntity;
import com.cicd.credentials.repository.ExternalRepoRepository;
import com.cicd.credentials.repository.SecretRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

/**
 * Service class handling the business logic for managing code repositories
 * and their associated authentication secrets.
 */
@Service
public class ExternalRepoService {

    private static final Logger log = LoggerFactory.getLogger(ExternalRepoService.class);
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
    public CreateRepoIdResponse create(String name, String url, UUID secretId) {
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
        repo =externalRepoRepository.save(repo);
        return new CreateRepoIdResponse(repo.getId());
    }

    /**
     * Retrieves a list of all External Repositories from the database.
     * @return A list of all entities of type RepoDetailsResponse
     */
    public List<RepoDetailsResponse> findAllDto() {
        List<ExternalRepoEntity> entities = externalRepoRepository.findAll();
        return entities.stream().map(entity -> {
            UUID  secId = null;
            String secName = null;
            if (entity.getSecret() != null) {
                secId = entity.getSecret().getId();
                secName = entity.getSecret().getName();
            }
            return new RepoDetailsResponse(entity.getId(), entity.getName(), entity.getUrl(), secId, secName);
        }).toList();
    }

    /**
     * Deletes a repository by its unique identifier.
     *
     * @param id the unique identifier of the repository to be deleted
     */
    public void delete(UUID id) {
        log.debug("Attempting to find repository with ID: {} before deletion", id);
        if (!externalRepoRepository.existsById(id)) {
            log.warn("Cannot delete repository. ID {} not found in database.", id);
            throw new RuntimeException("Repository not found");
        }
        externalRepoRepository.deleteById(id);
        log.info("Deleted repository entity from database (ID: {})", id);
    }

    /**
     * Updates an existing repository configuration and its associated secret relationship.
     * * @param id      The database ID of the repository to update.
     * @param request The DTO containing the updated name, URL, and secret ID.
     * @return The updated repository details mapped into a response DTO.
     */
    @Transactional
    public RepoDetailsResponse update(UUID id, UpdateRepoRequest request) {
       ExternalRepoEntity repository = externalRepoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Repository not found with id: " + id));

        repository.setName(request.name());
        repository.setUrl(request.url());

        String secretName = null;
        if (request.secretId() != null) {
            SecretEntity secret = secretRepository.findById(request.secretId() )
                    .orElseThrow(() -> new RuntimeException("Secret not found with id: " + request.secretId()));
            repository.setSecret(secret);
            secretName = secret.getName();
        } else {
            repository.setSecret(null);
        }
        ExternalRepoEntity savedRepo = externalRepoRepository.save(repository);

        return new RepoDetailsResponse(
                savedRepo.getId(),
                savedRepo.getName(),
                savedRepo.getUrl(),
                request.secretId(),
                secretName,
                false
        );
    }
}