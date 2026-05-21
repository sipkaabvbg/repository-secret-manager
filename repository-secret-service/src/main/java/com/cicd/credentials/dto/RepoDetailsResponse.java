package com.cicd.credentials.dto;

public record RepoDetailsResponse(Long id,
                                  String name,
                                  String url,
                                  Long secretId,
                                  String secretName,
                                  boolean editable) {
    public RepoDetailsResponse(Long id, String name, String url, Long secretId, String secretName) {
        this(id, name, url, secretId, secretName, false);
    }
}
