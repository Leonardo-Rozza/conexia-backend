package com.conexia.service.impl;

import com.conexia.exceptions.ResourceNotFoundException;
import com.conexia.persistence.entity.*;
import com.conexia.persistence.repository.*;
import com.conexia.service.dto.LoggedUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class SecurityService {
    private final InstitutionRepository institutionRepository;
    private final EmployerRepository employerRepository;
    private final GraduateRepository graduateRepository;
    private final JobOfferRepository jobOfferRepository;
    private final ApplicationRepository applicationRepository;
    private final CourseRepository courseRepository;

    public SecurityService(
            InstitutionRepository institutionRepository,
            EmployerRepository employerRepository,
            GraduateRepository graduateRepository,
            JobOfferRepository jobOfferRepository,
            ApplicationRepository applicationRepository,
            CourseRepository courseRepository
    ) {
        this.institutionRepository = institutionRepository;
        this.employerRepository = employerRepository;
        this.graduateRepository = graduateRepository;
        this.jobOfferRepository = jobOfferRepository;
        this.applicationRepository = applicationRepository;
        this.courseRepository = courseRepository;
    }

    private LoggedUser getLoggedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (LoggedUser) auth.getPrincipal();
    }

    private boolean isAdmin(LoggedUser logged) {
        return logged.role().equals("ROLE_ADMIN");
    }

    // ========== INSTITUTIONS ==========
    public boolean isInstitutionOwner(Long institutionId) {
        LoggedUser logged = getLoggedUser();
        if (isAdmin(logged)) return true;


        InstitutionEntity inst = institutionRepository.findById(institutionId)
                .orElseThrow(() -> new ResourceNotFoundException("Institución", institutionId));

        return inst.getUser().getId().equals(logged.userId());
    }

    // ========== EMPLOYERS ==========
    public boolean isEmployerOwner(Long employerId) {
        LoggedUser logged = getLoggedUser();
        if (isAdmin(logged)) return true;

        EmployerEntity emp = employerRepository.findById(employerId)
                .orElseThrow(() -> new ResourceNotFoundException("Empleador", employerId));

        return emp.getUser().getId().equals(logged.userId());
    }

    // ========== GRADUATES ==========
    public boolean isGraduateOwner(Long graduateId) {
        LoggedUser logged = getLoggedUser();
        if (isAdmin(logged)) return true;

        GraduateEntity grad = graduateRepository.findById(graduateId)
                .orElseThrow(() -> new ResourceNotFoundException("Egresado", graduateId));

        return grad.getUser().getId().equals(logged.userId());
    }

    // ===== OFERTA LABORAL =====
    public boolean isJobOfferOwner(Long offerId) {
        LoggedUser logged = getLoggedUser();
        if (isAdmin(logged)) return true;

        JobOfferEntity offer = jobOfferRepository.findById(offerId)
                .orElseThrow(() -> new ResourceNotFoundException("Oferta Laboral", offerId));

        return offer.getEmployer().getUser().getId().equals(logged.userId());
    }

    // Applications

    public boolean isEmployerOwnerOfApplication(Long applicationId) {
        LoggedUser logged = getLoggedUser();
        if (isAdmin(logged)) return true;

        ApplicationEntity app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Postulación", applicationId));

        return app.getJobOffer().getEmployer().getUser().getId().equals(logged.userId());
    }

    public boolean isCourseOwner(Long courseId) {
        LoggedUser logged = getLoggedUser();
        if (isAdmin(logged)) return true;

        CourseEntity course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Curso", courseId));

        return Objects.equals(course.getInstitution().getUser().getId(), logged.userId());
    }


}
