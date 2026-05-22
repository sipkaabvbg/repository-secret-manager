package com.cicd.credentials.entity;

import jakarta.persistence.*;

import java.util.UUID;

/**
 * Represents a source code repository managed by the CI/CD subsystem.
 * Stores the repository URL and the related authentication secret.
 */
@Entity
@Table(name = "repositories")
public class ExternalRepoEntity {

    /**
     * Unique identifier of the repository.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Human-readable repository name.
     */
    @Column(nullable = false)
    private String name;

    /**
     * Repository URL
     */
    @Column(nullable = false, unique = true)
    private String url;

    /**
     * Authentication secret associated with the repository.
     * Multiple repositories may reuse the same secret.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "secret_id", nullable = true)
    private SecretEntity secret;

    public ExternalRepoEntity() {
    }

    public UUID getId() {
        return id;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public SecretEntity getSecret() {
        return secret;
    }

    public void setSecret(SecretEntity secret) {
        this.secret = secret;
    }
}