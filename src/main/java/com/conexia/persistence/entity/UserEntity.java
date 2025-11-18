package com.conexia.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "users")
public class UserEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  private Long id;

  @Column(name = "username", unique = true, length = 50)
  private String username;

  @Column(name = "password", length = 200)
  private String password;

  @Column(name = "email", unique = true, length = 150)
  private String email;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "rol_id")
  private Rol rol;

  @Column(name = "is_active")
  private Boolean isActive = true;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @PrePersist
  public void prePersist() {
    this.createdAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
  }

  @PreUpdate
  public void preUpdate() {
    this.updatedAt = LocalDateTime.now();
  }
}
