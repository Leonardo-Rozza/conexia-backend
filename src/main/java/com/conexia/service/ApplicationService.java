package com.conexia.service;

import com.conexia.persistence.entity.enums.ApplicationStatus;
import com.conexia.service.dto.ApplicationCreateDTO;
import com.conexia.service.dto.ApplicationDTO;
import com.conexia.service.dto.ApplicationUpdateDTO;

import java.util.List;

public interface ApplicationService {

    ApplicationDTO apply(ApplicationCreateDTO dto);

    ApplicationDTO findById(Long id);

    List<ApplicationDTO> findAll();

    List<ApplicationDTO> getByGraduate(Long graduateId);

    List<ApplicationDTO> getByOffer(Long offerId);

    ApplicationDTO updateStatus(Long id, ApplicationUpdateDTO dto);

    List<ApplicationDTO> getByEmployer(Long employerId);
    List<ApplicationDTO> getByOfferAndStatus(Long offerId, ApplicationStatus status);
    void delete(Long id);

}
