package com.conexia.service.impl;

import com.conexia.persistence.entity.Rol;
import com.conexia.persistence.entity.UserEntity;
import com.conexia.persistence.repository.RolRepository;
import com.conexia.persistence.repository.UserRepository;
import com.conexia.service.dto.AuthLoginRequest;
import com.conexia.service.dto.AuthResponse;
import com.conexia.utils.jwt.JwtUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTest {

  @Mock
  private JwtUtils jwtUtils;

  @Mock
  private UserRepository userRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private RolRepository rolRepository;

  @InjectMocks
  private UserDetailsServiceImpl userDetailsService;

  @AfterEach
  void tearDown() {
    SecurityContextHolder.clearContext();
  }

  @Test
  void testLoginUser_SetsSecurityContextAndReturnsJwtToken() {
    // Arrange
    String username = "john";
    String rawPassword = "secret";
    String encodedPassword = "encoded-secret";
    String expectedJwt = "jwt-token";

    Rol rol = new Rol();
    rol.setName("USER");

    UserEntity userEntity = new UserEntity();
    userEntity.setUsername(username);
    userEntity.setPassword(encodedPassword);
    userEntity.setRol(rol);
    userEntity.setIsActive(true);

    when(userRepository.findUserEntityByUsername(username)).thenReturn(Optional.of(userEntity));
    when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);
    when(jwtUtils.createdToken(any(Authentication.class))).thenReturn(expectedJwt);

    AuthLoginRequest request = new AuthLoginRequest(username, rawPassword);

    // Act
    AuthResponse response = userDetailsService.loginUser(request);

    // Assert response
    assertNotNull(response);
    assertEquals(username, response.username());
    assertEquals("Autenticado con éxito", response.message());
    assertEquals(expectedJwt, response.jwt());
    assertTrue(response.status());

    // Assert SecurityContext
    Authentication contextAuth = SecurityContextHolder.getContext().getAuthentication();
    assertNotNull(contextAuth);
    assertTrue(contextAuth.isAuthenticated());
    Object principal = contextAuth.getPrincipal();
    assertTrue(principal instanceof UserDetails);
    UserDetails userDetails = (UserDetails) principal;
    assertEquals(username, userDetails.getUsername());
    assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("USER")));

    // Verify interactions
    verify(userRepository, times(1)).findUserEntityByUsername(username);
    verify(passwordEncoder, times(1)).matches(rawPassword, encodedPassword);

    ArgumentCaptor<Authentication> authCaptor = ArgumentCaptor.forClass(Authentication.class);
    verify(jwtUtils, times(1)).createdToken(authCaptor.capture());
    Authentication tokenAuth = authCaptor.getValue();
    assertNotNull(tokenAuth);
    assertTrue(((UserDetails) tokenAuth.getPrincipal()).getAuthorities().contains(new SimpleGrantedAuthority("USER")));
  }

    @Test
    void testLoginUser_ThrowsException_WhenUserNotFound() {
        AuthLoginRequest request = new AuthLoginRequest("nonexistent", "password");
        when(userRepository.findUserEntityByUsername("nonexistent")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loginUser(request));
        verify(userRepository).findUserEntityByUsername("nonexistent");
    }

    @Test
    void testLoginUser_ThrowsException_WhenPasswordInvalid() {
        // Arrange
        String username = "john";
        String rawPassword = "wrong";
        String encodedPassword = "encoded-secret";

        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword(encodedPassword);
        user.setIsActive(true);

        Rol rol = new Rol();
        rol.setName("USER");
        user.setRol(rol);

        when(userRepository.findUserEntityByUsername(username))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(rawPassword, encodedPassword))
                .thenReturn(false);

        AuthLoginRequest request = new AuthLoginRequest(username, rawPassword);

        // Act + Assert
        BadCredentialsException ex = assertThrows(BadCredentialsException.class,
                () -> userDetailsService.loginUser(request));

        assertEquals("La contraseña no es correcta.", ex.getMessage());

        verify(userRepository, times(1)).findUserEntityByUsername(username);
        verify(passwordEncoder, times(1)).matches(rawPassword, encodedPassword);
    }



}
