package com.conexia.persistence.entity;

import com.conexia.persistence.entity.enums.JobOfferStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "job_offers")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobOfferEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_offer")
  private Long idOffer;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "employer_id")
  private EmployerEntity employer;

  @Column(name = "title", length = 150, nullable = false)
  private String title;

  @Column(name = "description", columnDefinition = "TEXT")
  private String description;

  @Column(name = "requirements", columnDefinition = "TEXT")
  private String requirements;

  @Column(name = "publication_date")
  private LocalDate publicationDate;

  @Column(name = "closing_date")
  private LocalDate closingDate;

  @Enumerated(EnumType.STRING)
  @Column(name = "status")
  private JobOfferStatus status;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @PrePersist
  public void prePersist() {
    this.createdAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();

    if (this.publicationDate == null) {
      this.publicationDate = LocalDate.now();
    }
    if (this.status == null) {
      this.status = JobOfferStatus.ACTIVA;
    }
  }

  @PreUpdate
  public void preUpdate() {
    this.updatedAt = LocalDateTime.now();
  }

  public static class CourseEntity {
  }
}

