package com.conexia.persistence.repository;

import com.conexia.persistence.entity.JobOfferEntity;
import com.conexia.persistence.entity.enums.JobOfferStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobOfferRepository extends JpaRepository<JobOfferEntity, Long> {

    // Buscar una oferta específica que esté ACTIVA (para detalle público)
    Optional<JobOfferEntity> findByIdOfferAndStatus(Long idOffer, JobOfferStatus status);

    // Listar todas las ofertas por estado (por ejemplo, todas las ACTIVAS)
    List<JobOfferEntity> findAllByStatus(JobOfferStatus status);

    // Listar todas las ofertas de un empleador específico
    List<JobOfferEntity> findAllByEmployer_IdEmployer(Long employerId);

}
