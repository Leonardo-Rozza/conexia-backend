package com.conexia.service;

import com.conexia.persistence.entity.enums.JobOfferStatus;
import com.conexia.service.dto.JobOfferCreateDTO;
import com.conexia.service.dto.JobOfferDTO;
import com.conexia.service.dto.JobOfferUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface JobOfferService {

    List<JobOfferDTO> findAll();
    Page<JobOfferDTO> findAll(Pageable pageable);
    JobOfferDTO findById(Long id);
    JobOfferDTO findActiveById(Long id);
    List<JobOfferDTO> findActive();
    List<JobOfferDTO> findByEmployer(Long employerId);
    List<JobOfferDTO> findByStatus(JobOfferStatus status);
    JobOfferDTO create(JobOfferCreateDTO dto);
    JobOfferDTO update(Long id, JobOfferUpdateDTO dto);
    JobOfferDTO close(Long id);
    void delete(Long id);
}
