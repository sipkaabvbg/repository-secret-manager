package com.cicd.credentials.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "credential_types")
public class CredentialTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * TOKEN, BASIC, SSH
     */
    @Column(nullable = false, unique = true)
    private String code;

    /**
     * Human readable label
     */
    @Column(nullable = false)
    private String displayName;

    // Standard default constructor (Required by JPA)
    public CredentialTypeEntity() {
    }

    // Parameterized constructor for easy object creation
    public CredentialTypeEntity(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    // --- GETTERS & SETTERS ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}