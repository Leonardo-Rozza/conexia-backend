package com.conexia.service.impl;

import com.conexia.exceptions.BusinessException;
import com.conexia.exceptions.ResourceNotFoundException;
import com.conexia.persistence.entity.GraduateEntity;
import com.conexia.persistence.repository.GraduateRepository;
import com.conexia.service.GraduateService;
import com.conexia.service.dto.GraduateDTO;
import com.conexia.service.dto.GraduateUpdateDTO;
import com.conexia.utils.mapper.GraduateMapper;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class GraduateServiceImpl implements GraduateService {

    private final GraduateRepository graduateRepository;
    private final GraduateMapper graduateMapper;

    public GraduateServiceImpl(GraduateRepository graduateRepository, GraduateMapper graduateMapper) {
        this.graduateRepository = graduateRepository;
        this.graduateMapper = graduateMapper;
    }


    @Override
    public List<GraduateDTO> findAll() {
       return this.graduateRepository.findAll().stream()
               .map(graduateMapper::toDTO)
               .toList();
    }

    @Override
    public Page<GraduateDTO> findAll(Pageable pageable) {
        return this.graduateRepository.findAll(pageable)
                .map(this.graduateMapper::toDTO);
    }

    @Override
    public GraduateDTO findById(Long id) {
        return this.graduateRepository.findById(id)
                .map(this.graduateMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Graduado", id));
    }

    @Override
    @Transactional
    public GraduateDTO save(GraduateDTO graduateDTO) {

        if (this.graduateRepository.existsByEmail(graduateDTO.email())){
            throw new BusinessException("Email existente, ya fue creado por otro graduado.");
        }

        GraduateEntity graduateEntity = this.graduateMapper.toEntityForCreation(graduateDTO);
        GraduateEntity saved = this.graduateRepository.save(graduateEntity);

        return this.graduateMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public GraduateDTO update(Long id, GraduateUpdateDTO graduateUpdateDTO) {
        GraduateEntity graduateEntity = this.graduateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Graduado", id));

        if (graduateUpdateDTO.email() != null && !graduateUpdateDTO.email().trim().isEmpty()){
            boolean emailUsado = this.graduateRepository.existsByEmail(graduateUpdateDTO.email());
            boolean emailCambiado = !Objects.equals(graduateEntity.getEmail(), graduateUpdateDTO.email());

            if (emailUsado && emailCambiado){
                throw new BusinessException("El nuevo correo electrónico ya está siendo utilizado por otro Graduado.");
            }
        }

        this.graduateMapper.updateEntityFromDTO(graduateUpdateDTO, graduateEntity);
        GraduateEntity saved = this.graduateRepository.save(graduateEntity);

        return this.graduateMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (!this.graduateRepository.existsById(id)){
            throw new ResourceNotFoundException("No se encontró el graduado a eliminar.");
        }

        this.graduateRepository.deleteById(id);
    }
}
