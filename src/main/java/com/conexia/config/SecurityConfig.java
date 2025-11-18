package com.conexia.config;

import com.conexia.config.filter.JwtFilterValidation;
import com.conexia.service.impl.UserDetailsServiceImpl;
import com.conexia.utils.jwt.JwtUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

  private final JwtUtils jwtUtils;


  public SecurityConfig(JwtUtils jwtUtils) {
    this.jwtUtils = jwtUtils;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    return httpSecurity
            .csrf(AbstractHttpConfigurer::disable)
            .cors(Customizer.withDefaults())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(http -> {
                // ===== Endpoints públicos =====
                http.requestMatchers("/auth/**", "/error").permitAll();
                http.requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll();
                http.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();

                // ===== INSTITUCIONES =====
                // Listados solo ADMIN
                http.requestMatchers(HttpMethod.GET, "/api/institutions", "/api/institutions/paginated")
                        .hasRole("ADMIN");
                // Creación manual solo ADMIN
                http.requestMatchers(HttpMethod.POST, "/api/institutions/**")
                        .hasRole("ADMIN");
                // Acceso a recursos específicos (GET/PUT/DELETE /{id}) -> rol INSTITUCION o ADMIN
                http.requestMatchers("/api/institutions/**")
                        .hasAnyRole("INSTITUCION", "ADMIN");

                // ===== EMPLEADORES =====
                // Listados solo ADMIN
                http.requestMatchers(HttpMethod.GET, "/api/employers", "/api/employers/paginated")
                        .hasRole("ADMIN");
                // Creación manual solo ADMIN (si la tenés)
                http.requestMatchers(HttpMethod.POST, "/api/employers/**")
                        .hasRole("ADMIN");
                // Acceso a recursos específicos -> EMPLEADOR o ADMIN
                http.requestMatchers("/api/employers/**")
                        .hasAnyRole("EMPLEADOR", "ADMIN");

                // ===== EGRESADOS =====
                // Listados solo ADMIN
                http.requestMatchers(HttpMethod.GET, "/api/graduates", "/api/graduates/paginated")
                        .hasRole("ADMIN");
                // Creación manual solo ADMIN (si la tuvieras)
                http.requestMatchers(HttpMethod.POST, "/api/graduates/**")
                        .hasRole("ADMIN");
                // Acceso a recursos específicos -> EGRESADO o ADMIN
                http.requestMatchers("/api/graduates/**")
                        .hasAnyRole("EGRESADO", "ADMIN");

                // ===== OFERTAS LABORALES =====
                // ver ofertas activas (público / egresados)
                http.requestMatchers("/api/offers/active", "/api/offers/active/**")
                        .permitAll();

                // ver ofertas de un empleador, crear, actualizar, eliminar (EMPLEADOR o ADMIN)
                http.requestMatchers(HttpMethod.GET, "/api/offers/employer/**")
                        .hasAnyRole("EMPLEADOR", "ADMIN");
                http.requestMatchers(HttpMethod.POST, "/api/offers")
                        .hasAnyRole("EMPLEADOR", "ADMIN");
                http.requestMatchers(HttpMethod.PUT, "/api/offers/**")
                        .hasAnyRole("EMPLEADOR", "ADMIN");
                http.requestMatchers(HttpMethod.DELETE, "/api/offers/**")
                        .hasAnyRole("EMPLEADOR", "ADMIN");

                // ver todas las ofertas / paginadas (solo ADMIN)
                http.requestMatchers(HttpMethod.GET, "/api/offers", "/api/offers/paginated")
                        .hasRole("ADMIN");

                // ===== APPLICATIONS =====
                // Graduado se postula
                http.requestMatchers(HttpMethod.POST, "/api/applications")
                        .hasAnyRole("EGRESADO", "ADMIN");

                // Graduado ve sus postulaciones
                http.requestMatchers(HttpMethod.GET, "/api/applications/graduate/**")
                        .hasAnyRole("EGRESADO", "ADMIN");

                // Empleador ve postulaciones a su oferta
                http.requestMatchers(HttpMethod.GET, "/api/applications/offer/**")
                        .hasAnyRole("EMPLEADOR", "ADMIN");

                // Empleador/Admin actualizan estado
                http.requestMatchers(HttpMethod.PUT, "/api/applications/**")
                        .hasAnyRole("EMPLEADOR", "ADMIN");

                // ===== COURSES =====
                // Lectura (cualquier usuario autenticado)
                http.requestMatchers(HttpMethod.GET, "/api/courses", "/api/courses/**")
                        .permitAll();

                // Crear cursos: solo institución o admin
                http.requestMatchers(HttpMethod.POST, "/api/courses")
                        .hasAnyRole("INSTITUCION", "ADMIN");

                // Actualizar cursos: solo institución dueña o admin
                http.requestMatchers(HttpMethod.PUT, "/api/courses/**")
                        .hasAnyRole("INSTITUCION", "ADMIN");

                // Eliminar cursos: solo institución dueña o admin
                http.requestMatchers(HttpMethod.DELETE, "/api/courses/**")
                        .hasAnyRole("INSTITUCION", "ADMIN");

                // ===== Por defecto: cualquier otra cosa autenticada =====
                http.anyRequest().authenticated();
            })
            .addFilterBefore(new JwtFilterValidation(jwtUtils), UsernamePasswordAuthenticationFilter.class)
            .build();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  public AuthenticationProvider authenticationProvider(UserDetailsServiceImpl userDetailsService){
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(userDetailsService);
    provider.setPasswordEncoder(passwordEncoder());
    return provider;
  }

  @Bean
  public PasswordEncoder passwordEncoder(){
    return new BCryptPasswordEncoder();
  }
}
