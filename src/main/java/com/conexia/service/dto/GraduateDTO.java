package com.conexia.service.dto;

import com.conexia.persistence.entity.enums.EmploymentStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record GraduateDTO(
        Long idGraduate,
        Long userId,

        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 150, message = "El nombre no puede exceder 150 caracteres")
        String name,

        @NotBlank(message = "El apellido es obligatorio")
        @Size(max = 150, message = "El apellido no puede exceder 150 caracteres")
        String lastname,

        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El formato del email no es válido")
        @Size(max = 150, message = "El email no puede exceder 150 caracteres")
        String email,

        @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
        String phone,

        @NotNull(message = "La fecha de nacimiento es obligatoria")
        @Past(message = "La fecha de nacimiento debe ser en el pasado")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate birthday,

        @NotBlank(message = "La profesión es obligatoria")
        @Size(max = 100, message = "La profesión no puede exceder 100 caracteres")
        String profession,

        @NotBlank(message = "La institución de graduación es obligatoria")
        @Size(max = 200, message = "La institución de graduación no puede exceder 200 caracteres")
        String graduationInstitution,

        EmploymentStatus employmentStatus,

        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime createdAt,

        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime updatedAt
) {
}
