package com.cicd.credentials.validation.impl.github;

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

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public boolean supports(String provider, String authMethod) {
        return Provider.GITHUB.name().equalsIgnoreCase(provider)
                && AuthMethod.TOKEN.name().equalsIgnoreCase(authMethod);
    }

    @Override
    public ValidationResponse validate(String repo, String credential) {

        try {
            HttpHeaders headers = new HttpHeaders();

            headers.setBearerAuth(credential);
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            String url = toGithubApi(repo);

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                return new ValidationResponse(true, "Token is valid");
            }

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