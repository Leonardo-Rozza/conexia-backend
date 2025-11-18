//package com.conexia.presentation.controller;
//
//import com.conexia.service.dto.AuthCreatedUserRequest;
//import com.conexia.service.dto.AuthLoginRequest;
//import com.conexia.service.dto.AuthResponse;
//import com.conexia.service.impl.UserDetailsServiceImpl;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.context.TestConfiguration;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Import;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(controllers = AuthenticationController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
//@Import({AuthenticationControllerTest.TestConfig.class, AuthenticationControllerTest.TestExceptionHandler.class})
//@ActiveProfiles("test")
//public class AuthenticationControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private FakeUserDetailsServiceImpl fakeService;
//
//    @Test
//    public void shouldReturn200AndAuthResponseOnValidLogin() throws Exception {
//        String body = """
//                {
//                  "username": "john",
//                  "password": "secret123"
//                }
//                """;
//
//        mockMvc.perform(post("/auth/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(body))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.username").value("john"))
//                .andExpect(jsonPath("$.jwt").value("jwt-john"))
//                .andExpect(jsonPath("$.status").value(true))
//                .andExpect(jsonPath("$.message").value("Autenticado con éxito"));
//    }
//
//    @Test
//    public void shouldCreateGraduateUserAndReturn201WithRoleEgresado() throws Exception {
//        String body = """
//                {
//                  "username": "nuevoEgresado",
//                  "password": "password123",
//                  "email": "egresado@example.com"
//                }
//                """;
//
//        mockMvc.perform(post("/auth/register/graduate")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(body))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.username").value("nuevoEgresado"))
//                .andExpect(jsonPath("$.status").value(true));
//
//        Assertions.assertEquals("ROLE_EGRESADO", fakeService.getLastRole());
//    }
//
//    @Test
//    public void shouldCreateEmployerUserAndReturn201WithRoleEmpleador() throws Exception {
//        String body = """
//                {
//                  "username": "nuevoEmpleador",
//                  "password": "password123",
//                  "email": "empleador@example.com"
//                }
//                """;
//
//        mockMvc.perform(post("/auth/register/employer")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(body))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.username").value("nuevoEmpleador"))
//                .andExpect(jsonPath("$.status").value(true));
//
//        Assertions.assertEquals("ROLE_EMPLEADOR", fakeService.getLastRole());
//    }
//
//    @Test
//    public void shouldReturn401OnLoginWithInvalidCredentials() throws Exception {
//        String body = """
//                {
//                  "username": "invalid",
//                  "password": "whatever"
//                }
//                """;
//
//        mockMvc.perform(post("/auth/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(body))
//                .andExpect(status().isUnauthorized());
//    }
//
//    @Test
//    public void shouldReturn400OnGraduateRegistrationWhenUserAlreadyExists() throws Exception {
//        String body = """
//                {
//                  "username": "existing",
//                  "password": "password123",
//                  "email": "dup@example.com"
//                }
//                """;
//
//        mockMvc.perform(post("/auth/register/graduate")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(body))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    public void shouldReturn400OnInstitutionRegistrationWithInvalidRequestBody() throws Exception {
//        String body = """
//                {
//                  "username": "",
//                  "password": "123",
//                  "email": "not-an-email"
//                }
//                """;
//
//        mockMvc.perform(post("/auth/register/institution")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(body))
//                .andExpect(status().isBadRequest());
//    }
//
//    @TestConfiguration
//    static class TestConfig {
//        @Bean
//        public FakeUserDetailsServiceImpl userDetailsService() {
//            return new FakeUserDetailsServiceImpl();
//        }
//    }
//
//    static class FakeUserDetailsServiceImpl extends UserDetailsServiceImpl {
//
//        private String lastRole;
//
//        public FakeUserDetailsServiceImpl() {
//            super(null, null, null, null);
//        }
//
//        public String getLastRole() {
//            return lastRole;
//        }
//
//        @Override
//        public AuthResponse loginUser(AuthLoginRequest request) {
//            if ("invalid".equals(request.username()) || "wrong".equals(request.password())) {
//                throw new BadCredentialsException("Invalid credentials");
//            }
//            return new AuthResponse(request.username(), "Autenticado con éxito", "jwt-" + request.username(), true);
//        }
//
//        @Override
//        public AuthResponse createUser(AuthCreatedUserRequest userRequest, String roleName) {
//            this.lastRole = roleName;
//            if ("existing".equals(userRequest.username())) {
//                throw new IllegalArgumentException("User already exists");
//            }
//            return new AuthResponse(userRequest.username(), "Registro exitoso", "jwt-" + roleName, true);
//        }
//    }
//
//    @RestControllerAdvice
//    static class TestExceptionHandler {
//
//        @ExceptionHandler(BadCredentialsException.class)
//        public ResponseEntity<String> handleBadCredentials(BadCredentialsException ex) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
//        }
//
//        @ExceptionHandler(IllegalArgumentException.class)
//        public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
//        }
//
//        @ExceptionHandler(MethodArgumentNotValidException.class)
//        public ResponseEntity<String> handleValidation(MethodArgumentNotValidException ex) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Validation failed");
//        }
//    }
//}
