package com.conexia.persistence.repository;

import com.conexia.persistence.entity.UserEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.lang.ScopedValue;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

  Optional<UserEntity> findUserEntityByUsername(String username);

  boolean existsByUsername(String username);

  boolean existsByEmail(@NotBlank(message = "El correo electrónico es obligatorio.") @Email(message = "Debe ingresar un correo válido.") String email);

}
