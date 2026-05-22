package com.cicd.credentials.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;import java.util.UUID;

/**
 * Immutable DTO record to map the incoming repository creation payload.
 */
public record RepoRequest(
        @NotBlank(message = "Repository name cannot be empty")
        @Size(min = 2, max = 100, message = "Repository name must be between 2 and 100 characters")
        String name,
        String url,
        UUID secretId) {
}