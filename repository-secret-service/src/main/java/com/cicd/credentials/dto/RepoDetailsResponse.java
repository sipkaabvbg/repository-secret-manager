package com.cicd.credentials.dto;

import java.util.UUID;

public record RepoDetailsResponse(UUID id,
                                  String name,
                                  String url,
                                  UUID secretId,
                                  String secretName,
                                  boolean editable) {
    public RepoDetailsResponse(UUID id, String name, String url, UUID secretId, String secretName) {
        this(id, name, url, secretId, secretName, false);
    }
}
