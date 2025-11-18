package com.conexia.service.impl;

import com.conexia.exceptions.BusinessException;
import com.conexia.exceptions.ResourceNotFoundException;
import com.conexia.persistence.entity.InstitutionEntity;
import com.conexia.persistence.repository.InstitutionRepository;
import com.conexia.service.InstitutionService;
import com.conexia.service.dto.InstitutionDTO;
import com.conexia.service.dto.InstitutionUpdateDTO;
import com.conexia.utils.mapper.InstitutionMapper;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class InstitutionServiceImpl implements InstitutionService {

    private final InstitutionRepository institutionRepository;
    private final InstitutionMapper institutionMapper;

    public InstitutionServiceImpl(InstitutionRepository institutionRepository, InstitutionMapper institutionMapper) {
        this.institutionRepository = institutionRepository;
        this.institutionMapper = institutionMapper;
    }

    @Override
    public List<InstitutionDTO> findAll() {
        return this.institutionRepository.findAll().stream()
                .map(this.institutionMapper::toDTO)
                .toList();
    }

    @Override
    public Page<InstitutionDTO> findAll(Pageable pageable) {
        return institutionRepository.findAll(pageable)
                .map(institutionMapper::toDTO);
    }

    @Override
    public InstitutionDTO findById(Long id) {
        return this.institutionRepository.findById(id)
                .map(this.institutionMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Institución", id));
    }

    @Override
    @Transactional
    public InstitutionDTO save(InstitutionDTO institution) {
        if (institutionRepository.existsByEmail(institution.email())) {
            throw new BusinessException("Email existente, ya fue creado por otra institución.");
        }

        InstitutionEntity institutionEntity = this.institutionMapper.toEntityForCreation(institution);
        InstitutionEntity institutionEntitySaved = this.institutionRepository.save(institutionEntity);

        return this.institutionMapper.toDTO(institutionEntitySaved);
    }

    @Override
    @Transactional
    public InstitutionDTO update(Long id, InstitutionUpdateDTO institution) {

        InstitutionEntity existingInstitution = institutionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Institución", id));

        // Validar email duplicado solo si se está actualizando
        if (institution.email() != null && !institution.email().trim().isEmpty()) {
            boolean emailUsado = institutionRepository.existsByEmail(institution.email());
            boolean emailCambiado = !Objects.equals(existingInstitution.getEmail(), institution.email());

            if (emailUsado && emailCambiado) {
                throw new BusinessException("El nuevo correo electrónico ya está siendo utilizado por otra Institución.");
            }
        }

        this.institutionMapper.updateEntityFromDTO(institution, existingInstitution);
        InstitutionEntity saved = this.institutionRepository.save(existingInstitution);
        return this.institutionMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (!this.institutionRepository.existsById(id)){
            throw new ResourceNotFoundException("Institución", id);
        }
        this.institutionRepository.deleteById(id);
    }
}
