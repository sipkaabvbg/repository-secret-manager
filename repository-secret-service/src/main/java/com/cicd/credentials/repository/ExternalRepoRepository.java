package com.cicd.credentials.repository;


import com.cicd.credentials.entity.ExternalRepoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing {@link ExternalRepoEntity} entities.
 * Provides standard CRUD operations and database interaction capabilities thanks to Spring Data JPA.
 */
@Repository
public interface ExternalRepoRepository extends JpaRepository<ExternalRepoEntity, Long> {

    // Checks if at least one record associated with this Secret ID exists
    boolean existsBySecretId(Long secretId);
}