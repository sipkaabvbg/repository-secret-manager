package com.cicd.credentials.validation;

import com.cicd.credentials.dto.ValidationResponse;

/**
 * Strategy interface for validating credentials.
 * Each provider/auth combination has its own implementation.
 */
public interface CredentialValidator {

    /**
     * Checks if this validator supports given input.
     */
    boolean supports(String provider,String authMethod);

    /**
     * Performs validation against external system (e.g. GitHub API)
     */
    ValidationResponse validate(String repo, String credential);
}