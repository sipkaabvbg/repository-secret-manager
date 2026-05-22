package com.cicd.credentials.validation.impl.github;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.cicd.credentials.dto.ValidationResponse;
import com.cicd.credentials.validation.AuthMethod;
import com.cicd.credentials.validation.CredentialValidator;
import com.cicd.credentials.validation.Provider;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class GithubTokenValidator implements CredentialValidator {

    private static final Logger log = LoggerFactory.getLogger(GithubTokenValidator.class);
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public boolean supports(String provider, String authMethod) {
        return Provider.GITHUB.name().equalsIgnoreCase(provider)
                && AuthMethod.TOKEN.name().equalsIgnoreCase(authMethod);
    }

    @Override
    public ValidationResponse validate(String repo, String credential) {
        log.info("Starting validation for GitHub repository: {}", repo);
        try {
            HttpHeaders headers = new HttpHeaders();

            headers.setBearerAuth(credential);
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            String url = toGithubApi(repo);
            log.debug("Calling GitHub API at URL: {}", url);
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Successfully validated access to repository: {}", repo);
                return new ValidationResponse(true, "Token is valid");
            }
            log.warn("Validation failed for repository {}. GitHub returned status: {}", repo, response.getStatusCode());
            return new ValidationResponse(false, "Invalid token");

        } catch (Exception e) {
            return new ValidationResponse(false, e.getMessage());
        }
    }

    /**
     * Converts:
     * https://github.com/user/repo
     * to:
     * https://api.github.com/repos/user/repo
     */
    private String toGithubApi(String repoUrl) {
        String cleaned = repoUrl.replace("https://github.com/", "");
        if (cleaned.endsWith(".git")) {
            cleaned = cleaned.substring(0, cleaned.length() - 4);
        }
        return "https://api.github.com/repos/" + cleaned;
    }
}