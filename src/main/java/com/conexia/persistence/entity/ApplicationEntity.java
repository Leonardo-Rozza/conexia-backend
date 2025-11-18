package com.conexia.persistence.entity;


import com.conexia.persistence.entity.enums.ApplicationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "applications",
        uniqueConstraints = @UniqueConstraint(columnNames = {"graduate_id", "job_offer_id"})
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_application")
  private Long idApplication;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "graduate_id")
  private GraduateEntity graduate;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "job_offer_id")
  private JobOfferEntity jobOffer;

  @Column(name = "application_date")
  private LocalDate applicationDate;

  @Enumerated(EnumType.STRING)
  @Column(name = "status")
  private ApplicationStatus status;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @PrePersist
  public void prePersist() {
    this.createdAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();

    if (this.applicationDate == null) {
      this.applicationDate = LocalDate.now();
    }
    if (this.status == null) {
      this.status = ApplicationStatus.EN_PROCESO;
    }
  }

  @PreUpdate
  public void preUpdate() {
    this.updatedAt = LocalDateTime.now();
  }
}

