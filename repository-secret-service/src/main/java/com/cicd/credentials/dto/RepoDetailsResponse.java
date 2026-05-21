package com.cicd.credentials.dto;

public record RepoDetailsResponse(Long id,
                                  String name,
                                  String url,
                                  Long secretId,
                                  String secretName) {
}
