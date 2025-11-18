package com.conexia.service.dto;

import com.conexia.persistence.entity.enums.CourseModality;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CourseUpdateDTO(

        @Size(max = 200, message = "La longitud del titulo no puede superar los 200 caracteres.")
        String title,

        @Size(max = 2000, message = "La longitud de la descripción no puede superar los 2000 caracteres.")
        String description,

        CourseModality modality,

        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate startDate,

        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate endDate
) {
}
