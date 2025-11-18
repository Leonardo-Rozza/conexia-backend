package com.conexia.service.impl;

import com.conexia.exceptions.BusinessException;
import com.conexia.exceptions.ResourceNotFoundException;
import com.conexia.persistence.entity.EmployerEntity;
import com.conexia.persistence.entity.JobOfferEntity;
import com.conexia.persistence.entity.enums.JobOfferStatus;
import com.conexia.persistence.repository.EmployerRepository;
import com.conexia.persistence.repository.JobOfferRepository;
import com.conexia.service.JobOfferService;
import com.conexia.service.dto.JobOfferCreateDTO;
import com.conexia.service.dto.JobOfferDTO;
import com.conexia.service.dto.JobOfferUpdateDTO;
import com.conexia.utils.mapper.JobOfferMapper;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class JobOfferServiceImpl implements JobOfferService {

    private final JobOfferRepository jobOfferRepository;
    private final JobOfferMapper jobOfferMapper;
    private final EmployerRepository employerRepository;

    public JobOfferServiceImpl(
            JobOfferRepository jobOfferRepository,
            JobOfferMapper jobOfferMapper,
            EmployerRepository employerRepository
    ) {
        this.jobOfferRepository = jobOfferRepository;
        this.jobOfferMapper = jobOfferMapper;
        this.employerRepository = employerRepository;
    }

    @Override
    public List<JobOfferDTO> findAll() {
        return jobOfferRepository.findAll()
                .stream()
                .map(jobOfferMapper::toDTO)
                .toList();
    }

    @Override
    public Page<JobOfferDTO> findAll(Pageable pageable) {
        return jobOfferRepository.findAll(pageable)
                .map(jobOfferMapper::toDTO);
    }

    @Override
    public JobOfferDTO findById(Long id) {
        return jobOfferRepository.findById(id)
                .map(jobOfferMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Oferta Laboral", id));
    }

    @Override
    public JobOfferDTO findActiveById(Long id) {
        return jobOfferRepository.findByIdOfferAndStatus(id, JobOfferStatus.ACTIVA)
                .map(jobOfferMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Oferta laboral activa", id));
    }

    @Override
    public List<JobOfferDTO> findActive() {
        return jobOfferRepository.findAllByStatus(JobOfferStatus.ACTIVA)
                .stream()
                .map(jobOfferMapper::toDTO)
                .toList();
    }

    @Override
    public List<JobOfferDTO> findByEmployer(Long employerId) {
        return jobOfferRepository.findAllByEmployer_IdEmployer(employerId)
                .stream()
                .map(jobOfferMapper::toDTO)
                .toList();
    }

    @Override
    public List<JobOfferDTO> findByStatus(JobOfferStatus status) {
        return jobOfferRepository.findAllByStatus(status)
                .stream()
                .map(jobOfferMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional
    public JobOfferDTO create(JobOfferCreateDTO dto) {

        // Validar fechas
        if (dto.closingDate() != null && dto.closingDate().isBefore(LocalDate.now())) {
            throw new BusinessException("La fecha de cierre debe ser futura.");
        }

        EmployerEntity employer = employerRepository.findById(dto.employerId())
                .orElseThrow(() -> new BusinessException("El empleador no existe."));

        JobOfferEntity entity = jobOfferMapper.toEntityForCreation(dto);
        entity.setEmployer(employer);

        entity.setPublicationDate(LocalDate.now());
        entity.setStatus(JobOfferStatus.ACTIVA);

        return jobOfferMapper.toDTO(jobOfferRepository.save(entity));
    }

    @Override
    @Transactional
    public JobOfferDTO update(Long id, JobOfferUpdateDTO dto) {

        JobOfferEntity entity = jobOfferRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("La oferta no fue encontrada."));

        // Validación fechas
        if (dto.closingDate() != null && dto.closingDate().isBefore(LocalDate.now())) {
            throw new BusinessException("La fecha de cierre debe ser presente o futura.");
        }

        // Regla: una oferta VENCIDA no puede volver a ACTIVA
        if (dto.status() == JobOfferStatus.ACTIVA &&
                entity.getClosingDate().isBefore(LocalDate.now())) {
            throw new BusinessException("No se puede reactivar una oferta vencida.");
        }

        jobOfferMapper.updateEntityFromDTO(dto, entity);

        // Si cambia a INACTIVA → cerrar postulaciones lógicas (más adelante)
        if (dto.status() == JobOfferStatus.CERRADA) {
            entity.setStatus(JobOfferStatus.CERRADA);
        }

        return jobOfferMapper.toDTO(jobOfferRepository.save(entity));
    }

    @Override
    @Transactional
    public JobOfferDTO close(Long id) {

        JobOfferEntity entity = jobOfferRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Oferta Laboral", id));

        entity.setStatus(JobOfferStatus.CERRADA);

        return jobOfferMapper.toDTO(jobOfferRepository.save(entity));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!jobOfferRepository.existsById(id)) {
            throw new ResourceNotFoundException("Oferta Laboral", id);
        }
        jobOfferRepository.deleteById(id);
    }
}

