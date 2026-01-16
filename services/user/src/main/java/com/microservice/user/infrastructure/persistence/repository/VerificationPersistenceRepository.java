package com.microservice.user.infrastructure.persistence.repository;

import com.microservice.user.infrastructure.persistence.entity.VerificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationPersistenceRepository extends JpaRepository<VerificationEntity, Long> {
}
