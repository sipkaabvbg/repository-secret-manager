package com.cicd.credentials.dto;

import java.util.UUID;

public record ValidationRequest(String repoUrl, UUID secretId) {
}
