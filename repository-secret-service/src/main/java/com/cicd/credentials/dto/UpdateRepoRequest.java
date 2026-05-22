package com.cicd.credentials.dto;

import java.util.UUID;

/**
 * Data Transfer Object (DTO) received from the UI when a user edits an existing repository.
 */
public record UpdateRepoRequest(
        String name,
        String url,
        UUID secretId
) {}