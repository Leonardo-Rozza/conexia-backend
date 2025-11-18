package com.conexia.persistence.repository;

import com.conexia.persistence.entity.GraduateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GraduateRepository extends JpaRepository<GraduateEntity,Long > {
    boolean existsByEmail(String email);
}
