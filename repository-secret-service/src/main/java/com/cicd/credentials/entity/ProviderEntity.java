package com.cicd.credentials.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "providers")
public class ProviderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Example:
     * GitHub, GitLab, Bitbucket
     */
    @Column(nullable = false, unique = true)
    private String name;

    /**
     * Supported authentication methods.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "provider_credential_types",
            joinColumns = @JoinColumn(name = "provider_id"),
            inverseJoinColumns = @JoinColumn(name = "credential_type_id")
    )
    private Set<CredentialTypeEntity> supportedCredentialTypes = new HashSet<>();

    // Standard default constructor (Required by JPA)
    public ProviderEntity() {
    }

    // Parameterized constructor for easy object creation
    public ProviderEntity(String name) {
        this.name = name;
    }

    // --- GETTERS & SETTERS ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<CredentialTypeEntity> getSupportedCredentialTypes() {
        return supportedCredentialTypes;
    }

    public void setSupportedCredentialTypes(Set<CredentialTypeEntity> supportedCredentialTypes) {
        this.supportedCredentialTypes = supportedCredentialTypes;
    }
}