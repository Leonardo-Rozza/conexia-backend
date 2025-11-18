package com.conexia.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "employers")
public class EmployerEntity {

    public EmployerEntity(UserEntity user) {
        this.user = user;
        this.email = user.getEmail(); // opcional
    }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_employer")
  private Long idEmployer;

  @OneToOne
  @JoinColumn(name = "user_id", unique = true)
  private UserEntity user;

  @Column(name = "name_company", length = 150)
  private String nameCompany;

  @Column(name = "sector", length = 100)
  private String sector;

  @Column(name = "location", length = 150)
  private String location;

  @Column(name = "email", unique = true, length = 150)
  private String email;

  @Column(name = "phone", length = 20)
  private String phone;

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
