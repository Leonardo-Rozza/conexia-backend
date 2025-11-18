package com.conexia.service.dto;

import com.conexia.persistence.entity.enums.InstitutionType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public record InstitutionDTO(

        Long idInstitucion,

        Long userId,

        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 150, message = "El nombre no puede exceder 150 caracteres")
        String name,

        @NotNull(message = "El tipo de institución es obligatorio")
        InstitutionType typeInstitution,

        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El formato del email no es válido")
        @Size(max = 150, message = "El email no puede exceder 150 caracteres")
        String email,

        @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
        String phone,

        @Size(max = 200, message = "La dirección no puede exceder 200 caracteres")
        String address,

        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime createdAt,

        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime updatedAt
) {

}
