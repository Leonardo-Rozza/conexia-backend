package com.conexia.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "institutional_metrics")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InstitutionalMetricEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_metric")
  private Long idMetric;

  @OneToOne
  @JoinColumn(name = "id_institucion", unique = true)
  private InstitutionEntity institution;

  @Column(name = "total_registered_graduates")
  private Integer totalRegisteredGraduates;

  @Column(name = "total_employed_graduates")
  private Integer totalEmployedGraduates;

  @Column(name = "employment_rate", precision = 5, scale = 2)
  private BigDecimal employmentRate;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @PrePersist
  @PreUpdate
  public void preUpdate() {
    this.updatedAt = LocalDateTime.now();

    // Automatically calculate employment rate
    if (totalRegisteredGraduates != null && totalRegisteredGraduates > 0 && totalEmployedGraduates != null) {
      this.employmentRate = BigDecimal
              .valueOf((double) totalEmployedGraduates / totalRegisteredGraduates * 100)
              .setScale(2, BigDecimal.ROUND_HALF_UP);
    } else {
      this.employmentRate = BigDecimal.ZERO;
    }
  }
}
