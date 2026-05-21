package com.cicd.credentials.dto;

/**
 * Data Transfer Object (DTO) received from the UI when a user edits an existing repository.
 */
public record UpdateRepoRequest(
        String name,
        String url,
        Long secretId
) {}