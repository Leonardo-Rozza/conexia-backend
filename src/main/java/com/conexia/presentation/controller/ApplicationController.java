package com.conexia.presentation.controller;

import com.conexia.persistence.entity.enums.ApplicationStatus;
import com.conexia.service.ApplicationService;
import com.conexia.service.dto.ApplicationCreateDTO;
import com.conexia.service.dto.ApplicationDTO;
import com.conexia.service.dto.ApplicationUpdateDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@Tag(name = "Postulaciones", description = "Gestión de postulaciones a ofertas laborales")
public class ApplicationController {

    private final ApplicationService service;

    public ApplicationController(ApplicationService service) {
        this.service = service;
    }

    // =====================================================
    // 1. POSTULARSE – Graduado
    // =====================================================
    @PostMapping
    @Operation(summary = "Postularse a una oferta laboral")
    @PreAuthorize("hasRole('EGRESADO') or hasRole('ADMIN')")
    public ResponseEntity<ApplicationDTO> apply(@Valid @RequestBody ApplicationCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.apply(dto));
    }

    // =====================================================
    // 2. OBTENER POSTULACIÓN POR ID
    // =====================================================
    @GetMapping("/{id}")
    @Operation(summary = "Obtener una postulación por ID")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isGraduateOwnerOfApplication(#id) or @securityService.isEmployerOwnerOfApplication(#id)")
    public ResponseEntity<ApplicationDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    // =====================================================
    // 3. TODAS LAS POSTULACIONES (ADMIN)
    // =====================================================
    @GetMapping
    @Operation(summary = "Obtener todas las postulaciones (ADMIN)")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ApplicationDTO>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    // =====================================================
    // 4. Postulaciones de un graduado
    // =====================================================
    @GetMapping("/graduate/{graduateId}")
    @Operation(summary = "Obtener todas las postulaciones de un graduado")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isGraduateOwner(#graduateId)")
    public ResponseEntity<List<ApplicationDTO>> getByGraduate(@PathVariable Long graduateId) {
        return ResponseEntity.ok(service.getByGraduate(graduateId));
    }

    // =====================================================
    // 5. Postulaciones recibidas por una oferta
    // =====================================================
    @GetMapping("/offer/{offerId}")
    @Operation(summary = "Obtener postulaciones de una oferta laboral")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isJobOfferOwner(#offerId)")
    public ResponseEntity<List<ApplicationDTO>> getByOffer(@PathVariable Long offerId) {
        return ResponseEntity.ok(service.getByOffer(offerId));
    }

    // =====================================================
    // 6. Postulaciones de un empleador (todas sus ofertas)
    // =====================================================
    @GetMapping("/employer/{employerId}")
    @Operation(summary = "Obtener todas las postulaciones de un empleador")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isEmployerOwner(#employerId)")
    public ResponseEntity<List<ApplicationDTO>> getByEmployer(@PathVariable Long employerId) {
        return ResponseEntity.ok(service.getByEmployer(employerId));
    }

    // =====================================================
    // 7. Filtrar por estado dentro de una oferta
    // =====================================================
    @GetMapping("/offer/{offerId}/status/{status}")
    @Operation(summary = "Obtener postulaciones filtradas por estado para una oferta")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isJobOfferOwner(#offerId)")
    public ResponseEntity<List<ApplicationDTO>> getByOfferAndStatus(
            @PathVariable Long offerId,
            @PathVariable ApplicationStatus status
    ) {
        return ResponseEntity.ok(service.getByOfferAndStatus(offerId, status));
    }

    // =====================================================
    // 8. Actualizar estado
    // =====================================================
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar el estado de una postulación")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isEmployerOwnerOfApplication(#id)")
    public ResponseEntity<ApplicationDTO> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody ApplicationUpdateDTO dto
    ) {
        return ResponseEntity.ok(service.updateStatus(id, dto));
    }

    // =====================================================
    // 9. Eliminar postulación (solo graduado dueño o admin)
    // =====================================================
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una postulación")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isGraduateOwnerOfApplication(#id)")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}


