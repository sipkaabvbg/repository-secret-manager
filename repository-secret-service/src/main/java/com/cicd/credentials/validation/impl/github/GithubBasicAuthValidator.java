package com.cicd.credentials.validation.impl.github;

import com.cicd.credentials.dto.ValidationResponse;
import com.cicd.credentials.validation.CredentialValidator;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

@Component
public class GithubBasicAuthValidator implements CredentialValidator {

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public ValidationResponse validate(String repo, String credential) {

        try {
            HttpHeaders headers = new HttpHeaders();

            // Basic Auth = base64(username:password)
            String auth = "";//credential.getUsername() + ":" + credential.getSecret();
            String encoded = Base64.getEncoder().encodeToString(auth.getBytes());

            headers.set("Authorization", "Basic " + encoded);

            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    toGithubApi(repo),
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            return response.getStatusCode().is2xxSuccessful()
                    ? new ValidationResponse(true, "Credentials valid")
                    : new ValidationResponse(false, "Invalid credentials");

        } catch (Exception e) {
            return new ValidationResponse(false, e.getMessage());
        }
    }

    private String toGithubApi(String repoUrl) {
        String cleaned = repoUrl.replace("https://github.com/", "");
        return "https://api.github.com/repos/" + cleaned;
    }

}