package com.conexia.service.dto;

import com.conexia.persistence.entity.enums.ApplicationStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ApplicationDTO(

        Long idApplication,
        Long graduateId,
        Long offerId,

        ApplicationStatus status,

        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate applicationDate,

        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime createdAt,

        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime updatedAt

) {}
