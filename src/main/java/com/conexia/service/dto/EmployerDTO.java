package com.conexia.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record EmployerDTO(

        Long idEmployer,
        Long userId,

        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 150, message = "El nombre no puede exceder 150 caracteres")
        String nameCompany,

        @Size(max = 100, message = "El sector no puede exceder 100 caracteres")
        String sector,

        @Size(max = 150, message = "La ubicación no puede exceder 150 caracteres")
        String location,

        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El formato del email no es válido")
        @Size(max = 150, message = "El email no puede exceder 150 caracteres")
        String email,

        @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
        String phone,

        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime createdAt,

        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime updatedAt

) {
}
