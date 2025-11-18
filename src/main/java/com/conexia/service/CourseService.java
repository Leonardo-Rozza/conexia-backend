package com.conexia.service;

import com.conexia.service.dto.CourseCreateDTO;
import com.conexia.service.dto.CourseDTO;
import com.conexia.service.dto.CourseUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CourseService {

    List<CourseDTO> findAll();
    Page<CourseDTO> findAll(Pageable pageable);
    CourseDTO findById(Long id);
    CourseDTO save(CourseCreateDTO courseCreateDTO);
    CourseDTO update(Long id, CourseUpdateDTO courseUpdateDTO);
    void deleteById(Long id);
}
