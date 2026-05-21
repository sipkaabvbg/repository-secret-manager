package com.cicd.credentials.controller;

import com.cicd.credentials.dto.CreateRepoResponse;
import com.cicd.credentials.dto.RepoDetailsResponse;
import com.cicd.credentials.dto.UpdateRepoRequest;
import com.cicd.credentials.entity.ExternalRepoEntity;
import com.cicd.credentials.service.ExternalRepoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller providing web service endpoints for managing external source
 * code repositories.
 * Actively consumed by the SAPUI5 frontend.
 */
@RestController
@RequestMapping("/repositories")

public class ExternalRepoController {

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
     * @return ResponseEntity containing the ID of the newly created repository and HTTP 201 status
     */
    @PostMapping
    public ResponseEntity<CreateRepoResponse> create(@RequestBody com.cicd.credentials.dto.RepoRequest request) {
        ExternalRepoEntity createdRepo = externalRepoService.create(request.name(),request.url(), request.secretId());
        return ResponseEntity.status(HttpStatus.CREATED).body(new CreateRepoResponse(createdRepo.getId()));
    }
    /**
     * Endpoint to retrieve all currently persisted external repository URLs.
     * Returns HTTP Status 200 (OK) by default with the JSON list.
     * @return a list of all registered repositories
     */
    @GetMapping
    public List<RepoDetailsResponse> getAll() {
        List<ExternalRepoEntity> entities = externalRepoService.findAll();

        return entities.stream().map(entity -> {
            Long secId = null;
            String secName = null;
            if (entity.getSecret() != null) {
                secId = entity.getSecret().getId();
                secName = entity.getSecret().getName();
            }
            return new RepoDetailsResponse(entity.getId(), entity.getName(),entity.getUrl(), secId, secName);
        }).toList();
    }

    /**
     * Endpoint to delete a persisted repository URL by its unique identifier.
     * Returns HTTP Status 204 (No Content) to indicate a successful deletion
     * with no return body.
     * @param id the unique identifier of the repository to be deleted
     * @return ResponseEntity with HTTP 204 No Content status on successful deletion
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        externalRepoService.delete(id);
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
            @PathVariable Long id,
            @RequestBody UpdateRepoRequest request
    ) {
        RepoDetailsResponse updatedRepo = externalRepoService.update(id, request);
        return ResponseEntity.ok(updatedRepo);
    }
}