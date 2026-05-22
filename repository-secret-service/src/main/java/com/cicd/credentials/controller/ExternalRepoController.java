package com.cicd.credentials.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.cicd.credentials.dto.CreateRepoIdResponse;
import com.cicd.credentials.dto.RepoDetailsResponse;
import com.cicd.credentials.dto.UpdateRepoRequest;
import com.cicd.credentials.service.ExternalRepoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST Controller providing web service endpoints for managing external source
 * code repositories.
 * Actively consumed by the SAPUI5 frontend.
 */
@RestController
@RequestMapping("/repositories")

public class ExternalRepoController {

    private static final Logger log = LoggerFactory.getLogger(ExternalRepoController.class);
    private final ExternalRepoService externalRepoService;

    /**
     * Constructor injection for the ExternalRepoService business layer.
     * @param repositoryService the service handling repository business logic
     */
    public ExternalRepoController(ExternalRepoService repositoryService) {
        this.externalRepoService = repositoryService;
    }

    /**
     * EEndpoint to register and persist a new repository URL linked to an authentication secret.
     * Accepts a JSON record payload from the frontend.
     * @param request the repository creation request payload
     * @return ID of the newly created repository and HTTP 201 status
     */
    @PostMapping
    public ResponseEntity<CreateRepoIdResponse> create(@RequestBody com.cicd.credentials.dto.RepoRequest request) {
        log.info("Received request to create new repository with URL: {}", request.url());
        CreateRepoIdResponse createdRepo = externalRepoService.create(request.name(),request.url(), request.secretId());
        log.info("Successfully created repository with ID: {}", createdRepo.id());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRepo);
    }
    /**
     * Endpoint to retrieve all currently persisted external repository URLs.
     * Returns HTTP Status 200 (OK) by default with the JSON list.
     * @return a list of all registered repositories
     */
    @GetMapping
    public List<RepoDetailsResponse> getAll() {
        log.info("Received request to fetch all persisted repositories");
        List<RepoDetailsResponse> repositories = externalRepoService.findAllDto();
        log.info("Successfully retrieved {} repositories", repositories.size());
        return  repositories;
    }

    /**
     * Endpoint to delete a persisted repository URL by its unique identifier.
     * Returns HTTP Status 204 (No Content) to indicate a successful deletion
     * with no return body.
     * @param id the unique identifier of the repository to be deleted
     * @return ResponseEntity DTO with HTTP 204 No Content status on successful deletion
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        log.info("Received request to delete repository with ID: {}", id);
        externalRepoService.delete(id);
        log.info("Repository with ID: {} was successfully deleted", id);
        return ResponseEntity.noContent().build();
    }

    /**
     * REST endpoint to update an existing repository's details by its unique identifier.
     * This handles HTTP PUT requests aimed at "/repositories/{id}".
     *
     * @param id      The unique ID of the repository to be updated, extracted directly from the URL path.
     * @param request The data transfer object (DTO) containing the updated repository fields from the UI.
     * @return A ResponseEntity containing the updated repository details and an HTTP 200 OK status.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RepoDetailsResponse> updateRepository(
            @PathVariable UUID id,
            @RequestBody UpdateRepoRequest request
    ) {
        log.info("Received request to update repository with ID: {}. New URL: {}", id, request.url());
        RepoDetailsResponse updatedRepo = externalRepoService.update(id, request);
        log.info("Repository with ID: {} was successfully updated", id);
        return ResponseEntity.ok(updatedRepo);
    }
}