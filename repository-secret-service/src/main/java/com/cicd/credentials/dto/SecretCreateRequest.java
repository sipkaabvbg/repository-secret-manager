package com.cicd.credentials.dto;

/**
 * DTO record for receiving secret creation data from the frontend.
 */
public record SecretCreateRequest(
        String name,
        String secretValue,
        String provider,
        String secretType
) {}