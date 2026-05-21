package com.cicd.credentials.validation.impl.github;

import com.cicd.credentials.dto.ValidationResponse;
import com.cicd.credentials.validation.CredentialValidator;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class GithubTokenValidator implements CredentialValidator {

    private final RestTemplate restTemplate = new RestTemplate();

  /**  @Override
    public boolean supports(Provider provider, AuthMethod authMethod) {
        // This validator handles ONLY GitHub + Token auth
        return provider == Provider.GITHUB
                && authMethod == AuthMethod.TOKEN;
    }
*/
    @Override
    public ValidationResponse validate(String repo, String credential) {

        try {
            HttpHeaders headers = new HttpHeaders();

            // Bearer token authentication
           // headers.setBearerAuth(credential.getSecret());
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
        return "https://api.github.com/repos/" + cleaned;
    }
}