package com.conexia;

import com.conexia.persistence.entity.Rol;
import com.conexia.persistence.entity.UserEntity;
import com.conexia.persistence.entity.enums.RoleName;
import com.conexia.persistence.repository.RolRepository;
import com.conexia.persistence.repository.UserRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class ConexiaBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConexiaBackendApplication.class, args);
	}

  @Bean
  @Profile("!test")
  CommandLineRunner init(UserRepository userRepository,
                         RolRepository rolRepository,
                         PasswordEncoder passwordEncoder) {

    return args -> {

      Rol rolAdmin = rolRepository.findByName("ROLE_" + RoleName.ADMIN.name())
              .orElseGet(() -> rolRepository.save(Rol.builder()
                      .name("ROLE_" + RoleName.ADMIN.name())
                      .description("Administrador con acceso total al sistema")
                      .build()));

      Rol rolGraduate = rolRepository.findByName("ROLE_" + RoleName.EGRESADO.name())
              .orElseGet(() -> rolRepository.save(Rol.builder()
                      .name("ROLE_" + RoleName.EGRESADO.name())
                      .description("Usuario egresado con acceso a ofertas y perfil")
                      .build()));

      Rol rolInstitution = rolRepository.findByName("ROLE_" + RoleName.INSTITUCION.name())
              .orElseGet(() -> rolRepository.save(Rol.builder()
                      .name("ROLE_" + RoleName.INSTITUCION.name())
                      .description("Institución educativa con acceso a sus cursos y métricas")
                      .build()));

      Rol rolEmployer = rolRepository.findByName("ROLE_" + RoleName.EMPLEADOR.name())
              .orElseGet(() -> rolRepository.save(Rol.builder()
                      .name("ROLE_" + RoleName.EMPLEADOR.name())
                      .description("Empleador con acceso a publicación de ofertas laborales")
                      .build()));

      if (!userRepository.existsByUsername("Leonardo")) {
        UserEntity user = UserEntity.builder()
                .username("Leonardo")
                .password(passwordEncoder.encode("1234!!"))
                .email("Leo@gmail.com")
                .isActive(true)
                .rol(rolAdmin)
                .build();

        userRepository.save(user);
        System.out.println("Usuario administrador creado: Leonardo (ROLE_ADMIN)");
      } else {
        System.out.println("Usuario Leonardo ya existe, no se crea nuevamente.");
      }
    };

  }

}
