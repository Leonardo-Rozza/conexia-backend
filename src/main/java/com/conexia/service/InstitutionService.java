package com.conexia.service;


import com.conexia.service.dto.InstitutionDTO;
import com.conexia.service.dto.InstitutionUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface InstitutionService {

    List<InstitutionDTO> findAll();
    Page<InstitutionDTO> findAll(Pageable pageable);
    InstitutionDTO findById(Long id);
    InstitutionDTO save(InstitutionDTO institutionDTO);
    InstitutionDTO update(Long id, InstitutionUpdateDTO institutionUpdateDTO);
    void deleteById(Long id);
}
