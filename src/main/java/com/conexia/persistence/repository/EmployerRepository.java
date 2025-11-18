package com.conexia.persistence.repository;

import com.conexia.persistence.entity.EmployerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployerRepository extends JpaRepository<EmployerEntity, Long> {
    boolean existsByEmail(String email);
}
