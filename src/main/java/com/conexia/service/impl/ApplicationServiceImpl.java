package com.conexia.service.impl;

import com.conexia.exceptions.BusinessException;
import com.conexia.exceptions.ResourceNotFoundException;
import com.conexia.persistence.entity.*;
import com.conexia.persistence.entity.enums.ApplicationStatus;
import com.conexia.persistence.entity.enums.JobOfferStatus;
import com.conexia.persistence.repository.*;
import com.conexia.service.ApplicationService;
import com.conexia.service.dto.*;
import com.conexia.utils.mapper.ApplicationMapper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final GraduateRepository graduateRepository;
    private final JobOfferRepository jobOfferRepository;
    private final ApplicationMapper mapper;

    public ApplicationServiceImpl(
            ApplicationRepository applicationRepository,
            GraduateRepository graduateRepository,
            JobOfferRepository jobOfferRepository,
            ApplicationMapper mapper
    ) {
        this.applicationRepository = applicationRepository;
        this.graduateRepository = graduateRepository;
        this.jobOfferRepository = jobOfferRepository;
        this.mapper = mapper;
    }

    // ===========================================
    // ===========   APLICAR A OFERTA   ==========
    // ===========================================
    @Override
    public ApplicationDTO apply(ApplicationCreateDTO dto) {

        // No permitir duplicados
        if (applicationRepository.existsByGraduate_IdGraduateAndJobOffer_IdOffer(dto.graduateId(), dto.offerId())) {
            throw new BusinessException("Ya existe una postulación para esta oferta.");
        }

        GraduateEntity grad = graduateRepository.findById(dto.graduateId())
                .orElseThrow(() -> new ResourceNotFoundException("Graduado", dto.graduateId()));

        JobOfferEntity offer = jobOfferRepository.findById(dto.offerId())
                .orElseThrow(() -> new ResourceNotFoundException("Oferta Laboral", dto.offerId()));

        // Oferta debe estar activa
        if (offer.getStatus() != JobOfferStatus.ACTIVA) {
            throw new BusinessException("La oferta laboral no está activa.");
        }

        // Oferta no puede estar vencida
        if (offer.getClosingDate() != null &&
                offer.getClosingDate().isBefore(LocalDate.now())) {
            throw new BusinessException("La oferta está vencida.");
        }

        // Crear entidad
        ApplicationEntity entity = mapper.toEntityForCreation(dto);
        entity.setGraduate(grad);
        entity.setJobOffer(offer);
        entity.setStatus(ApplicationStatus.EN_PROCESO);

        return mapper.toDTO(applicationRepository.save(entity));
    }

    // ===========================================
    // =============   GET BY ID   ===============
    // ===========================================

    @Override
    public ApplicationDTO findById(Long id) {
        return applicationRepository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Postulación", id));
    }

    // ===========================================
    // =============   GET ALL   =================
    // ===========================================

    @Override
    public List<ApplicationDTO> findAll() {
        return applicationRepository.findAll()
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    // ===========================================
    // ==========   GET BY GRADUATE   ============
    // ===========================================

    @Override
    public List<ApplicationDTO> getByGraduate(Long graduateId) {
        return applicationRepository.findAllByGraduate_IdGraduate(graduateId)
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    // ===========================================
    // ===========   GET BY OFFER   ==============
    // ===========================================

    @Override
    public List<ApplicationDTO> getByOffer(Long offerId) {
        return applicationRepository.findAllByJobOffer_IdOffer(offerId)
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    // ===========================================
    // =============   UPDATE STATUS   ============
    // ===========================================

    @Override
    public ApplicationDTO updateStatus(Long id, ApplicationUpdateDTO dto) {

        ApplicationEntity entity = applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Postulación", id));

        JobOfferEntity offer = entity.getJobOffer();

        // NO permitir modificar si la oferta está cerrada/vencida
        if (offer.getStatus() == JobOfferStatus.CERRADA ||
                (offer.getClosingDate() != null && offer.getClosingDate().isBefore(LocalDate.now()))) {
            throw new BusinessException("No se puede cambiar el estado porque la oferta está cerrada o vencida.");
        }

        // Reglas de transiciones
        if (entity.getStatus() == ApplicationStatus.ACEPTADO &&
                dto.status() != ApplicationStatus.ACEPTADO) {
            throw new BusinessException("Una aplicación aceptada no puede volver atrás.");
        }

        if (entity.getStatus() == ApplicationStatus.RECHAZADO &&
                dto.status() == ApplicationStatus.ACEPTADO) {
            throw new BusinessException("No se puede aceptar una aplicación que ya fue rechazada.");
        }

        mapper.updateEntityFromDTO(dto, entity);

        ApplicationEntity saved = applicationRepository.save(entity);

        return mapper.toDTO(saved);
    }

    @Override
    public void delete(Long id) {
        ApplicationEntity app = applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Postulación", id));

        applicationRepository.delete(app);
    }

    @Override
    public List<ApplicationDTO> getByEmployer(Long employerId) {
        return applicationRepository.findAllByJobOffer_Employer_IdEmployer(employerId)
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public List<ApplicationDTO> getByOfferAndStatus(Long offerId, ApplicationStatus status) {
        return applicationRepository.findAllByJobOffer_IdOfferAndStatus(offerId, status)
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

}


