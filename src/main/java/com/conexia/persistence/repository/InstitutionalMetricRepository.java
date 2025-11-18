package com.conexia.persistence.repository;

import com.conexia.persistence.entity.InstitutionalMetricEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstitutionalMetricRepository extends JpaRepository<InstitutionalMetricEntity, Long> {
}
