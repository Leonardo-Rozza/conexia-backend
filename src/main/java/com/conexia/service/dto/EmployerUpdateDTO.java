package com.conexia.service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record EmployerUpdateDTO(
        @Size(max = 150, message = "El nombre de la empresa no puede exceder 150 caracteres")
        String nameCompany,

        @Size(max = 100, message = "El sector no puede exceder 100 caracteres")
        String sector,

        @Size(max = 150, message = "La ubicación no puede exceder 150 caracteres")
        String location,

        @Email(message = "El formato del email no es válido")
        @Size(max = 150, message = "El email no puede exceder 150 caracteres")
        String email,

        @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
        String phone
) {
}
