package com.conexia.service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AuthLoginRequest(

        @NotNull(message = "El usuario no puede estar vacío.")
        @Size(min = 4, max = 50, message = "El largo del nombre no puede superar los 50 caractéres.")
        String username,

        @NotNull(message = "El password no puede estar vacío.")
        @Size(min = 4, message = "La contraseña debe tener al menos 8 caracteres.")
        String password


) {
}
