package com.conexia.service.dto;

import com.conexia.persistence.entity.enums.JobOfferStatus;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record JobOfferUpdateDTO(

        @Size(max = 200, message = "El título no puede exceder 200 caracteres")
        String title,

        @Size(max = 2000, message = "La descripción no puede exceder 2000 caracteres")
        String description,

        @Size(max = 1000, message = "Los requisitos no pueden exceder 1000 caracteres")
        String requirements,

        @FutureOrPresent(message = "La fecha de cierre debe ser presente o futura")
        LocalDate closingDate,

        JobOfferStatus status

) {}
