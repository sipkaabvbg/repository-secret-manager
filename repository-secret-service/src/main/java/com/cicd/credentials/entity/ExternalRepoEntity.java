package com.cicd.credentials.entity;

import jakarta.persistence.*;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    public Long getId() {
        return id;
    }

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