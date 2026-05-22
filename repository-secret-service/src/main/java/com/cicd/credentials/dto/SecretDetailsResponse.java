package com.cicd.credentials.dto;

public record SecretDetailsResponse(Long id,
                                    String name,
                                    String provider,
                                    boolean editable) {
}
