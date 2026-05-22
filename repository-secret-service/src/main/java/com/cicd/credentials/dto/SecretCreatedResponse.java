package com.cicd.credentials.dto;

/**
 * DTO record for returning only the ID of the created secret to the frontend.
 */
public record SecretCreatedResponse(Long id,boolean editable) {}