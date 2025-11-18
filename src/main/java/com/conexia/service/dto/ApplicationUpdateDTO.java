package com.conexia.service.dto;

import com.conexia.persistence.entity.enums.ApplicationStatus;

public record ApplicationUpdateDTO(
        ApplicationStatus status
) {}
