package com.conexia.service.impl;

import com.conexia.exceptions.BusinessException;
import com.conexia.exceptions.ResourceNotFoundException;
import com.conexia.persistence.entity.EmployerEntity;
import com.conexia.persistence.repository.EmployerRepository;
import com.conexia.service.EmployerService;
import com.conexia.service.dto.EmployerDTO;
import com.conexia.service.dto.EmployerUpdateDTO;
import com.conexia.utils.mapper.EmployerMapper;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class EmployerServiceImpl implements EmployerService {

    private final EmployerRepository employerRepository;
    private final EmployerMapper employerMapper;

    public EmployerServiceImpl(EmployerRepository employerRepository, EmployerMapper employerMapper) {
        this.employerRepository = employerRepository;
        this.employerMapper = employerMapper;
    }

    @Override
    public List<EmployerDTO> findAll() {
        return this.employerRepository.findAll().stream()
                .map(this.employerMapper::toDto)
                .toList();
    }

    @Override
    public Page<EmployerDTO> findAll(Pageable pageable) {
        return this.employerRepository.findAll(pageable)
                .map(this.employerMapper::toDto);
    }

    @Override
    public EmployerDTO findById(Long id) {
        return this.employerRepository.findById(id)
                .map(this.employerMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Empleador", id));
    }

    @Override
    @Transactional
    public EmployerDTO save(EmployerDTO employerDTO) {
        if (this.employerRepository.existsByEmail(employerDTO.email())) {
            throw new BusinessException("Email existente, ya fue creado por otro empleador.");
        }

        EmployerEntity employerEntity = this.employerMapper.toEntityForCreation(employerDTO);
        EmployerEntity saved = this.employerRepository.save(employerEntity);

        return this.employerMapper.toDto(saved);
    }

    @Override
    @Transactional
    public EmployerDTO update(Long id, EmployerUpdateDTO employerUpdateDTO) {
        EmployerEntity employerEntity = this.employerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empleador", id));

        // Validar email duplicado solo si se está actualizando
        if (employerUpdateDTO.email() != null && !employerUpdateDTO.email().trim().isEmpty()) {
            boolean emailUsado = employerRepository.existsByEmail(employerUpdateDTO.email());
            boolean emailCambiado = !Objects.equals(employerEntity.getEmail(), employerUpdateDTO.email());

            if (emailUsado && emailCambiado) {
                throw new BusinessException("El nuevo correo electrónico ya está siendo utilizado por otro empleador.");
            }
        }

        this.employerMapper.updateEntityFromDTO(employerUpdateDTO, employerEntity);
        EmployerEntity saved = this.employerRepository.save(employerEntity);

        return this.employerMapper.toDto(saved);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (!this.employerRepository.existsById(id)){
            throw new ResourceNotFoundException("No se encontró el empleador a eliminar.");
        }
        this.employerRepository.deleteById(id);
    }
}
