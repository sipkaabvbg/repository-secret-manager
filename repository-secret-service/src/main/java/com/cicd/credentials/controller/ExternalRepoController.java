package com.cicd.credentials.controller;

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
     * Endpoint to register and persist a new repository URL linked to an authentication secret.
     * Accepts a JSON record payload from the frontend.
     * @return ResponseEntity containing the created ExternalRepoEntity and HTTP 201 status
     */
    @PostMapping
    public ResponseEntity<ExternalRepoEntity> create(@RequestBody com.cicd.credentials.dto.RepoRequest request) {
        ExternalRepoEntity createdRepo = externalRepoService.create(request.url(), request.secretId());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRepo);
    }
    /**
     * Endpoint to retrieve all currently persisted external repository URLs.
     * Returns HTTP Status 200 (OK) by default with the JSON list.
     * @return a list of all registered repositories
     */
    @GetMapping
    public List<ExternalRepoEntity> getAll() {
        return externalRepoService.findAll();
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
}