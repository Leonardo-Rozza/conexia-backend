package com.conexia.service;

import com.conexia.service.dto.GraduateDTO;
import com.conexia.service.dto.GraduateUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GraduateService {

    List<GraduateDTO> findAll();
    Page<GraduateDTO> findAll(Pageable pageable);
    GraduateDTO findById(Long id);
    GraduateDTO save(GraduateDTO graduateDTO);
    GraduateDTO update(Long id, GraduateUpdateDTO graduateUpdateDTO);
    void deleteById(Long id);
}
