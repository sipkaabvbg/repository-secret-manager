package com.cicd.credentials.dto;

public record ValidationRequest(String repoUrl, Long secretId) {
}
