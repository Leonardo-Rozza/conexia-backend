package com.conexia.service.impl;

import com.conexia.persistence.entity.*;
import com.conexia.persistence.repository.*;
import com.conexia.service.dto.AuthCreatedUserRequest;
import com.conexia.service.dto.AuthLoginRequest;
import com.conexia.service.dto.AuthResponse;
import com.conexia.utils.jwt.JwtUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  private final JwtUtils jwtUtils;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final RolRepository rolRepository;
  private final InstitutionRepository institutionRepository;
  private final GraduateRepository graduateRepository;
  private final EmployerRepository employerRepository;


    public UserDetailsServiceImpl(JwtUtils jwtUtils, UserRepository userRepository, PasswordEncoder passwordEncoder, RolRepository rolRepository, InstitutionRepository institutionRepository, GraduateRepository graduateRepository, EmployerRepository employerRepository
    ) {
    this.jwtUtils = jwtUtils;
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.rolRepository = rolRepository;
    this.institutionRepository = institutionRepository;
    this.graduateRepository = graduateRepository;
    this.employerRepository = employerRepository;
    }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    UserEntity userEntity = userRepository.findUserEntityByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("El usuario '" + username + "' no existe."));

    var authority = Collections.singletonList(new SimpleGrantedAuthority(userEntity.getRol().getName().toUpperCase()));

    return new User(
            userEntity.getUsername(),
            userEntity.getPassword(),
            userEntity.getIsActive(),
            true,
            true,
            true,
            authority
    );
  }

  public AuthResponse loginUser (AuthLoginRequest request){
      String username = request.username();
      String password = request.password();

    Authentication authentication = this.authenticate(username, password);
    SecurityContextHolder.getContext().setAuthentication(authentication);

    String accessToken = this.jwtUtils.createdToken(authentication);

    return new AuthResponse(username, "Autenticado con éxito", accessToken, true);
  }

  public Authentication authenticate(String username, String password){
      UserDetails userDetails = this.loadUserByUsername(username);

      if (userDetails == null){
        throw new BadCredentialsException("El usuario no existe.");
      }

      if (!passwordEncoder.matches(password, userDetails.getPassword())){
        throw new BadCredentialsException("La contraseña no es correcta.");
      }

      return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
  }

  public AuthResponse createUser(AuthCreatedUserRequest userRequest, String roleName){

    if (userRepository.existsByUsername((userRequest.username()))) {
      throw new IllegalArgumentException("El nombre de usuario ya está registrado.");
    }

    if (userRepository.existsByEmail(userRequest.email())) {
      throw new IllegalArgumentException("El correo electrónico ya está registrado.");
    }

    Rol rol = rolRepository.findByName(roleName)
            .orElseThrow(() -> new IllegalArgumentException("El rol " + roleName + " no existe."));

    UserEntity user = UserEntity.builder()
            .username(userRequest.username())
            .password(passwordEncoder.encode(userRequest.password()))
            .email(userRequest.email())
            .rol(rol)
            .isActive(true)
            .build();

    userRepository.save(user);

    switch (rol.getName()) {
        case "ROLE_INSTITUCION" -> institutionRepository.save(new InstitutionEntity(user));
        case "ROLE_EGRESADO" -> graduateRepository.save(new GraduateEntity(user));
        case "ROLE_EMPLEADOR" -> employerRepository.save(new EmployerEntity(user));
    }

    var authority = Collections.singletonList(new SimpleGrantedAuthority(rol.getName()));
    UserDetails userDetails = new User(
            user.getUsername(),
            user.getPassword(),
            authority
    );
    Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null,authority);

    String accessToken = jwtUtils.createdToken(authentication);

    return new AuthResponse(
            user.getUsername(),
            "Registro exitoso",
            accessToken,
            true
    );
  }
}
