package com.cicd.credentials.dto;

import java.util.UUID;

public record SecretDetailsResponse(UUID id,
                                    String name,
                                    String provider,
                                    String secretType,
                                    boolean editable) {
}
