package com.cicd.credentials.validation.impl.github;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.cicd.credentials.dto.ValidationResponse;
import com.cicd.credentials.validation.AuthMethod;
import com.cicd.credentials.validation.CredentialValidator;
import com.cicd.credentials.validation.Provider;

import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;

@Component
public class GithubSshValidator implements CredentialValidator {

    private static final Logger log = LoggerFactory.getLogger(GithubSshValidator.class);

    @Override
    public boolean supports(String provider, String authMethod) {
        return Provider.GITHUB.name().equalsIgnoreCase(provider)
                && AuthMethod.SSH.name().equalsIgnoreCase(authMethod);
    }

    @Override
    public ValidationResponse validate(String repo, String privateKey) {
        log.info("Starting validation for GitHub repository: {}", repo);
        File keyFile = null;
        try {
            keyFile = File.createTempFile("github-key", ".pem");

            try (FileWriter writer = new FileWriter(keyFile)) {
                writer.write(privateKey);
            }
            keyFile.setReadable(false, false);
            keyFile.setReadable(true, true);
            String command = String.format(
                    "GIT_SSH_COMMAND='ssh -i %s -o StrictHostKeyChecking=no' git ls-remote %s",
                    keyFile.getAbsolutePath(),
                    repo
            );

            ProcessBuilder builder = new ProcessBuilder(
                    "bash",
                    "-c",
                    command
            );
            Process process = builder.start();

            int exitCode = process.waitFor();

            if (exitCode == 0) {
                return new ValidationResponse(true, "SSH key is valid");
            }
            String error = new String(process.getErrorStream().readAllBytes());

            return new ValidationResponse(false, error);

        } catch (Exception e) {
            return new ValidationResponse(false, e.getMessage());
        } finally {
            if (keyFile != null) {
                keyFile.delete();
            }
        }
    }
}