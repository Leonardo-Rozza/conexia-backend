package com.conexia.persistence.entity;


import com.conexia.persistence.entity.enums.InstitutionType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "institutions")
public class InstitutionEntity {

 public InstitutionEntity(UserEntity user) {
     this.user = user;
     this.email = user.getEmail();
 }


  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_institucion")
  private Long idInstitucion;

  @OneToOne
  @JoinColumn(name = "user_id", unique = true)
  private UserEntity user;

  @Column(name = "name", length = 150)
  private String name;

  @Enumerated(EnumType.STRING)
  @Column(name = "type_institution")
  private InstitutionType typeInstitution;

  @Column(name = "email", unique = true, length = 150)
  private String email;

  @Column(name = "phone", length = 20)
  private String phone;

  @Column(name = "address", length = 200)
  private String address;

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
