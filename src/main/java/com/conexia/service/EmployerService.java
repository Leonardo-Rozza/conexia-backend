package com.conexia.service;

import com.conexia.service.dto.EmployerDTO;
import com.conexia.service.dto.EmployerUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmployerService {

    List<EmployerDTO> findAll();
    Page<EmployerDTO> findAll(Pageable pageable);
    EmployerDTO findById(Long id);
    EmployerDTO save(EmployerDTO employerDTO);
    EmployerDTO update(Long id, EmployerUpdateDTO employerUpdateDTO);
    void deleteById(Long id);

}
