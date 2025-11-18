package com.conexia.service.impl;

import com.conexia.exceptions.BusinessException;
import com.conexia.exceptions.ResourceNotFoundException;
import com.conexia.persistence.entity.CourseEntity;
import com.conexia.persistence.entity.InstitutionEntity;
import com.conexia.persistence.repository.CourseRepository;
import com.conexia.persistence.repository.InstitutionRepository;
import com.conexia.service.CourseService;
import com.conexia.service.dto.CourseCreateDTO;
import com.conexia.service.dto.CourseDTO;
import com.conexia.service.dto.CourseUpdateDTO;
import com.conexia.utils.mapper.CourseMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseMapper courseMapper;
    private final CourseRepository courseRepository;
    private final InstitutionRepository institutionRepository;

    public CourseServiceImpl(
            CourseMapper courseMapper,
            CourseRepository courseRepository,
            InstitutionRepository institutionRepository
    ) {
        this.courseMapper = courseMapper;
        this.courseRepository = courseRepository;
        this.institutionRepository = institutionRepository;
    }

    @Override
    public List<CourseDTO> findAll() {
        return courseRepository.findAll()
                .stream()
                .map(courseMapper::toDTO)
                .toList();
    }

    @Override
    public Page<CourseDTO> findAll(Pageable pageable) {
        return courseRepository.findAll(pageable)
                .map(courseMapper::toDTO);
    }

    @Override
    public CourseDTO findById(Long id) {
        return courseRepository.findById(id)
                .map(courseMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Curso", id));
    }

    @Override
    public CourseDTO save(CourseCreateDTO dto) {

        InstitutionEntity institution = institutionRepository.findById(dto.idInstitution())
                .orElseThrow(() -> new ResourceNotFoundException("Institución", dto.idInstitution()));

        validateDates(dto.startDate(), dto.endDate());

        CourseEntity entity = courseMapper.toEntityForCreation(dto);
        entity.setInstitution(institution);

        return courseMapper.toDTO(courseRepository.save(entity));
    }

    @Override
    public CourseDTO update(Long id, CourseUpdateDTO dto) {

        CourseEntity entity = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Curso", id));

        validateDates(dto.startDate(), dto.endDate());

        courseMapper.updateEntityFromDTO(dto, entity);

        return courseMapper.toDTO(courseRepository.save(entity));
    }

    @Override
    public void deleteById(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Curso", id);
        }
        courseRepository.deleteById(id);
    }

    private void validateDates(LocalDate start, LocalDate end) {
        if (start != null && end != null && end.isBefore(start)) {
            throw new BusinessException("La fecha de fin no puede ser anterior a la fecha de inicio.");
        }
        if (end != null && end.isBefore(LocalDate.now())) {
            throw new BusinessException("La fecha de fin debe ser futura.");
        }
    }
}

