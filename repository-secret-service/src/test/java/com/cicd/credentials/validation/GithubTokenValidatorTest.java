package com.cicd.credentials.validation;

import com.cicd.credentials.dto.ValidationResponse;
import com.cicd.credentials.validation.impl.github.GithubTokenValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GithubTokenValidatorTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private GithubTokenValidator validator;

    @Test
    void testSupports_ShouldReturnTrueForGithubToken() {
        assertTrue(validator.supports("GITHUB", "TOKEN"));
    }

    @Test
    void testSupports_ShouldReturnFalseForOtherProviders() {
        assertFalse(validator.supports("GITLAB", "TOKEN"));
        assertFalse(validator.supports("GITHUB", "SSH"));
    }

    @Test
    void testValidate_Success() {
        // Arrange: Mock the REST call to return 200 OK
        ResponseEntity<String> mockResponse = new ResponseEntity<>("{}", HttpStatus.OK);

        when(restTemplate.exchange(
                any(String.class),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(String.class)
        )).thenReturn(mockResponse);
        ValidationResponse response = validator.validate("https://github.com/test/repo", "fake-token");

        assertTrue(response.valid());
        assertEquals("Token is valid and has access to the repository", response.message());
    }

    @Test
    void testValidate_Failure_Unauthorized() {
        // Arrange: Mock the REST call to return 401 Unauthorized
        ResponseEntity<String> mockResponse = new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        when(restTemplate.exchange(
                any(String.class),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(String.class)
        )).thenReturn(mockResponse);
        ValidationResponse response = validator.validate("https://github.com/test/repo", "wrong-token");
        assertFalse(response.valid());
    }
}