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
     */
    @Column(nullable = false)
    private String name;

    /**
     * Encrypted secret value.
     */
    @Column(nullable = false)
    private String secretValue;

    /**
     * The provider for this secret.
     */
    @Column(nullable = false)
    private String provider;

    /**
     * The authentication type.
     */
    @Column(nullable = false)
    private String secretType;

    public SecretEntity() {
    }
    public SecretEntity(String name, String secretValue, String provider, String secretType) {
        this.name = name;
        this.secretValue = secretValue;
        this.provider = provider;
        this.secretType = secretType;
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

    public void setSecretValue(String secretValue) {
        this.secretValue = secretValue;
    }

    public String getProvider() { return provider; }

    public void setProvider(String provider) { this.provider = provider;}

    public String getSecretType() {return secretType;}

    public void setSecretType(String secretType) { this.secretType = secretType; }
}