package com.conexia.persistence.repository;

import com.conexia.persistence.entity.ApplicationEntity;
import com.conexia.persistence.entity.enums.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<ApplicationEntity, Long> {
    boolean existsByGraduate_IdGraduateAndJobOffer_IdOffer(Long graduateId, Long jobOfferId);

    List<ApplicationEntity> findAllByGraduate_IdGraduate(Long graduateId);

    List<ApplicationEntity> findAllByJobOffer_IdOffer(Long offerId);

    // Todas las postulaciones recibidas por un empleador
    List<ApplicationEntity> findAllByJobOffer_Employer_IdEmployer(Long employerId);

    // Filtrar postulaciones por estado dentro de una oferta
    List<ApplicationEntity> findAllByJobOffer_IdOfferAndStatus(Long offerId, ApplicationStatus status);
}
