package com.conexia.service.dto;

import com.conexia.persistence.entity.enums.CourseModality;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CourseCreateDTO(

        @NotNull(message = "El ID de la institución es obligatorio.")
        Long idInstitution,

        @NotBlank(message = "El titulo del curso es obligatorio.")
        @Size(max = 200, message = "La longitud del titulo no puede superar los 200 caracteres.")
        String title,

        @NotBlank(message = "La descripción del curso es obligatorio.")
        @Size(max = 2000, message = "La longitud del titulo no puede superar los 2000 caracteres.")
        String description,

        @NotNull(message = "La modalidad del curso tiene que ser obligatoria.")
        CourseModality modality,

        @NotNull(message = "La fecha de inicio es obligatoria.")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate startDate,

        @Future(message = "La fecha de cierre debe ser futura.")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate endDate
) {
}
