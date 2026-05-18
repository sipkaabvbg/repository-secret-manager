package com.cicd.credentials.entity;

import jakarta.persistence.*;

/**
 * Represents an authentication secret used for accessing repositories.
 * The secret value should be stored encrypted.
 */
@Entity
@Table(name = "secrets")
public class SecretEntity {

    /**
     * Unique identifier of the secret.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Human-readable secret name.
     * Example: github-token
     */
    @Column(nullable = false)
    private String name;

    /**
     * Encrypted secret value.
     */
    @Column(nullable = false)
    private String secretValue;

    public SecretEntity() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSecretValue() {
        return secretValue;
    }

    public void setSecretValueValue(String secretValue) {
        this.secretValue = secretValue;
    }
}