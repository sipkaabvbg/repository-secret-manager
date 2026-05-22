package com.cicd.credentials.dto;

import java.util.UUID;

/**
 * DTO record for returning only the ID of the created secret to the frontend.
 */
public record SecretCreatedResponse(UUID id, boolean editable) {}