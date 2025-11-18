package com.conexia.service.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record JobOfferCreateDTO(

        @NotNull(message = "El ID del empleador es obligatorio")
        Long employerId,

        @NotBlank(message = "El título es obligatorio")
        @Size(max = 200, message = "El título no puede exceder 200 caracteres")
        String title,

        @NotBlank(message = "La descripción es obligatoria")
        @Size(max = 2000, message = "La descripción no puede exceder 2000 caracteres")
        String description,

        @NotBlank(message = "Los requisitos son obligatorios")
        @Size(max = 1000, message = "Los requisitos no pueden exceder 1000 caracteres")
        String requirements,

        @NotNull(message = "La fecha de cierre es obligatoria")
        @Future(message = "La fecha de cierre debe ser una fecha futura")
        LocalDate closingDate

) {}
