package com.cicd.credentials.dto;

/**
 * Immutable DTO record to map the incoming repository creation payload.
 */
public record RepoRequest(String name,String url, Long secretId) {
}