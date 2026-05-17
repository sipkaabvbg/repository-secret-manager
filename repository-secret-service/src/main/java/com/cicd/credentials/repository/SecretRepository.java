package com.cicd.credentials.repository;

import com.cicd.credentials.entity.SecretEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing {@link SecretEntity} entities.
 * Provides standard CRUD operations and database interaction capabilities thanks to Spring Data JPA.
 */
@Repository
public interface SecretRepository extends JpaRepository<SecretEntity, Long> {
}