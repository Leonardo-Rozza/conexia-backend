package com.conexia.service.dto;

import com.conexia.persistence.entity.enums.EmploymentStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;

import java.time.LocalDate;


public record GraduateUpdateDTO(

        @Size(max = 150, message = "El nombre no puede exceder 150 caracteres")
        String name,

        @Size(max = 150, message = "El apellido no puede exceder 150 caracteres")
        String lastname,

        @Email(message = "El formato del email no es válido")
        @Size(max = 150, message = "El email no puede exceder 150 caracteres")
        String email,

        @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
        String phone,

        @Past(message = "La fecha de nacimiento debe ser en el pasado")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate birthday,

        @Size(max = 100, message = "La profesión no puede exceder 100 caracteres")
        String profession,

        @Size(max = 200, message = "La institución de graduación no puede exceder 200 caracteres")
        String graduationInstitution,

        EmploymentStatus employmentStatus

) {
}
