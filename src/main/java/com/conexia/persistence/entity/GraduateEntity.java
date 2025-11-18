package com.conexia.persistence.entity;

import com.conexia.persistence.entity.enums.EmploymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "graduates")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GraduateEntity {

    public GraduateEntity(UserEntity user) {
        this.user = user;
        this.email = user.getEmail(); // opcional
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_graduate")
    private Long idGraduate;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private UserEntity user;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "last_name", length = 100)
    private String lastName;

    @Column(name = "email", unique = true, length = 150)
    private String email;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "birthday")
    private LocalDate birthday;

    @Column(name = "profession", length = 150)
    private String profession;

    @Column(name = "graduation_institution", length = 150)
    private String graduationInstitution;

    @Enumerated(EnumType.STRING)
    @Column(name = "employment_status")
    private EmploymentStatus employmentStatus;

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
