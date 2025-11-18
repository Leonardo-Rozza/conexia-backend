package com.conexia.service.dto;

import com.conexia.persistence.entity.enums.InstitutionType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record InstitutionUpdateDTO(
        @Size(max = 150, message = "El nombre no puede exceder 150 caracteres")
        String name,

        InstitutionType typeInstitution,

        @Email(message = "El formato del email no es válido")
        @Size(max = 150, message = "El email no puede exceder 150 caracteres")
        String email,

        @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
        String phone,

        @Size(max = 200, message = "La dirección no puede exceder 200 caracteres")
        String address
) {
}
