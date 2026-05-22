package com.cicd.credentials.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
/**
 * DTO record for receiving secret creation data from the frontend.
 */
public record SecretCreateRequest(
        @NotBlank(message = "Secret name cannot be empty")
        @Size(min = 2, max = 50, message = "Secret name must be between 2 and 50 characters")
        String name,

        @NotBlank(message = "Secret value cannot be empty")
        @Size(min = 5, max = 2000, message = "Secret value is too short or too long")
        String secretValue,

        @NotBlank(message = "Provider is required")
        @Pattern(
                regexp = "^(?i)(GITHUB|GITLAB|BITBUCKET)$",
                message = "Unsupported provider. Allowed types are: GITHUB, GITLAB, BITBUCKET"
        )
        String provider,
        String secretType
) {}