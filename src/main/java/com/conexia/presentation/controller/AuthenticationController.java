package com.conexia.presentation.controller;

import com.conexia.persistence.entity.enums.RoleName;
import com.conexia.service.dto.AuthCreatedUserRequest;
import com.conexia.service.dto.AuthLoginRequest;
import com.conexia.service.dto.AuthResponse;
import com.conexia.service.impl.UserDetailsServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticación", description = "Controlador de Autenticación")
public class AuthenticationController {

  private final UserDetailsServiceImpl userDetailsService;

  public AuthenticationController(UserDetailsServiceImpl userDetailsService) {
    this.userDetailsService = userDetailsService;
  }

    @Operation(summary = "Iniciar sesión", description = "Autentica un usuario con usuario/contraseña y devuelve un token JWT en la respuesta.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autenticación correcta",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class),
                            examples = @ExampleObject(value = "{\"accessToken\":\"eyJhbGci...\",\"tokenType\":\"Bearer\",\"username\":\"Leonardo\"}")
                    )),
            @ApiResponse(responseCode = "400", description = "Petición inválida (datos de entrada no válidos)",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Credenciales incorrectas",
                    content = @Content)
    })
  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthLoginRequest userRequest){
    return new ResponseEntity<>(this.userDetailsService.loginUser(userRequest), HttpStatus.OK);
  }

    @Operation(summary = "Registrar egresado", description = "Crea un usuario con rol Egresado y devuelve una respuesta con el token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario creado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class),
                            examples = @ExampleObject(value = "{\"accessToken\":\"eyJhbGci...\",\"tokenType\":\"Bearer\",\"username\":\"nuevoEgresado\"}")
                    )),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o usuario ya existe",
                    content = @Content)
    })
  @PostMapping("/register/graduate")
  public ResponseEntity<AuthResponse> registerEgresado(@RequestBody @Valid AuthCreatedUserRequest request) {
    AuthResponse response = userDetailsService.createUser(request, "ROLE_" + RoleName.EGRESADO);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

    @Operation(summary = "Registrar institución", description = "Crea un usuario con rol Institución y devuelve una respuesta con el token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario creado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o usuario ya existe",
                    content = @Content)
    })
  @PostMapping("/register/institution")
  public ResponseEntity<AuthResponse> registerInstitucion(@RequestBody @Valid AuthCreatedUserRequest request) {
    AuthResponse response = userDetailsService.createUser(request, "ROLE_" + RoleName.INSTITUCION);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

    @Operation(summary = "Registrar empleador", description = "Crea un usuario con rol Empleador y devuelve una respuesta con el token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario creado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o usuario ya existe",
                    content = @Content)
    })
  @PostMapping("/register/employer")
  public ResponseEntity<AuthResponse> registerEmpleador(@RequestBody @Valid AuthCreatedUserRequest request) {
    AuthResponse response = userDetailsService.createUser(request, "ROLE_" + RoleName.EMPLEADOR);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }



}
