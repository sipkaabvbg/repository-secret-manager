package com.cicd.credentials.validation;

import com.cicd.credentials.dto.ValidationResponse;

public interface CredentialValidator {
    public ValidationResponse validate(String repo, String credential);
}
