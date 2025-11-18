package com.conexia.service.dto;

import com.conexia.persistence.entity.enums.JobOfferStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record JobOfferDTO(

        Long idOffer,

        Long employerId,

        String title,

        String description,

        String requirements,

        LocalDate publicationDate,

        LocalDate closingDate,

        JobOfferStatus status,

        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime createdAt,

        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime updatedAt
) {}
