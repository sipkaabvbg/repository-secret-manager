package com.cicd.credentials.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.cicd.credentials.dto.ValidationResponse;
import com.cicd.credentials.entity.SecretEntity;
import com.cicd.credentials.repository.SecretRepository;
import com.cicd.credentials.validation.CredentialValidator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ValidationService {

    private static final Logger log = LoggerFactory.getLogger(ValidationService.class);
    /**
     * Spring injects ALL validators automatically.
     */
    private final List<CredentialValidator> validators;
    private final SecretRepository secretRepository;

    public ValidationService(List<CredentialValidator> validators, SecretRepository secretRepository) {
        this.validators = validators;
        this.secretRepository = secretRepository;
    }

 public ValidationResponse validate(
         String repoUrl,
         UUID credentialId) {
     log.info("Starting validation logic for repository: {} using credential ID: {}", repoUrl, credentialId);
     SecretEntity secret = secretRepository.findById(credentialId)
             .orElseThrow(() -> new RuntimeException("Secret not found"));
     // Find matching strategy at runtime
     CredentialValidator validator = validators.stream()
             .filter(v -> v.supports(
                     secret.getProvider(),
                     secret.getSecretType()
             ))
             .findFirst()
             .orElseThrow(() ->
                     new RuntimeException("No validator found")
             );
     // Delegate validation to selected strategy
     return validator.validate(repoUrl, secret.getSecretValue());
  }
}
