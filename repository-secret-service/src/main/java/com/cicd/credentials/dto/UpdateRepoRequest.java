package com.cicd.credentials.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

/**
 * Data Transfer Object (DTO) received from the UI when a user edits an existing repository.
 */
public record UpdateRepoRequest(
        @NotBlank(message = "Repository name cannot be empty")
        @Size(min = 2, max = 100, message = "Repository name must be between 2 and 100 characters")
        String name,
        String url,
        UUID secretId
) {}