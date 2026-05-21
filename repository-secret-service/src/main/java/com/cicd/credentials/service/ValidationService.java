package com.cicd.credentials.service;

import com.cicd.credentials.dto.ValidationResponse;
import com.cicd.credentials.entity.SecretEntity;
import com.cicd.credentials.repository.SecretRepository;
import com.cicd.credentials.validation.CredentialValidator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ValidationService {

    /**
     * Spring injects ALL validators automatically.
     * This is a registry of strategies.
     */
    private final List<CredentialValidator> validators;
    private final SecretRepository secretRepository;

    public ValidationService(List<CredentialValidator> validators, SecretRepository secretRepository) {
        this.validators = validators;
        this.secretRepository = secretRepository;
    }

 /**   public ValidationResponse validate(String repoUrl, String credential) {

        // Find matching strategy at runtime
        CredentialValidator validator = validators.stream()
                .filter(v -> v.supports(
                        credential.getProvider(),
                        credential.getAuthMethod()
                ))
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException("No validator found")
                );

        // Delegate validation to selected strategy
        return validator.validate(repoUrl, credential);
    }
  */
 public ValidationResponse validate(
         String repoUrl,
         Long credentialId)
  {
     SecretEntity secret = secretRepository.findById(credentialId)
             .orElseThrow(() -> new RuntimeException("Secret not found with ID: " + credentialId));
   String credential=secret.getSecretValue();
     // Iterate through all registered validators
     for (CredentialValidator validator : validators) {

         // Check if validator supports current credential type
      /**  if (validator.supports(
                 credential.getProvider(),
                 credential.getAuthMethod()
         )) {
*/if(validator.getClass().getName().contains("GithubTokenValidator")){
             // Delegate validation to matching strategy
          //
             return validator.validate(repoUrl, credential);
  //   return validator.validate(repoUrl, secret.getSecretValue());
     }
     }
      return null;
}}
