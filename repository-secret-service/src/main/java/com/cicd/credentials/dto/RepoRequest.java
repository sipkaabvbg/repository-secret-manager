package com.cicd.credentials.dto;

import java.util.UUID;

/**
 * Immutable DTO record to map the incoming repository creation payload.
 */
public record RepoRequest(String name,String url, UUID secretId) {
}