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
    private String value;

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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}