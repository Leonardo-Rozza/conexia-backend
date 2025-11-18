package com.conexia.persistence.entity;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
public class Rol {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_rol")
  private Long id;

  @Column(name = "name", unique = true, length = 50)
  private String name;

  @Column(name = "description", length = 100)
  private String description;
}