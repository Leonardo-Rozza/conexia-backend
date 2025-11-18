package com.conexia.service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthCreatedUserRequest(

        @NotBlank(message = "El nombre de usuario es obligatorio.")
        @Size(min = 4, max = 50, message = "El nombre de usuario debe tener entre 4 y 50 caracteres.")
        String username,

        @NotBlank(message = "La contraseña es obligatoria.")
        @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres.")
        String password,

        @NotBlank(message = "El correo electrónico es obligatorio.")
        @Email(message = "Debe ingresar un correo válido.")
        String email
) {
}
