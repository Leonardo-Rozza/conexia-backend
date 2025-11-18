package com.conexia.presentation.controller;

import com.conexia.persistence.entity.enums.JobOfferStatus;
import com.conexia.service.JobOfferService;
import com.conexia.service.dto.JobOfferCreateDTO;
import com.conexia.service.dto.JobOfferDTO;
import com.conexia.service.dto.JobOfferUpdateDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/offers")
@Tag(name = "Ofertas Laborales", description = "Gestión completa de ofertas laborales")
public class JobOfferController {

    private final JobOfferService jobOfferService;

    public JobOfferController(JobOfferService jobOfferService) {
        this.jobOfferService = jobOfferService;
    }


    // 1. EGRESADOS / PÚBLICO – CONSULTAR OFERTAS ACTIVAS
    @Operation(
            summary = "Obtener todas las ofertas activas",
            description = "Devuelve únicamente las ofertas activas y vigentes."
    )
    @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    @GetMapping("/active")
    public ResponseEntity<List<JobOfferDTO>> getActiveOffers() {
        return ResponseEntity.ok(jobOfferService.findActive());
    }

    @Operation(
            summary = "Obtener detalles de una oferta activa",
            description = "Devuelve los datos de una oferta que debe estar en estado ACTIVA."
    )
    @ApiResponse(responseCode = "200", description = "Oferta activa encontrada")
    @GetMapping("/active/{id}")
    public ResponseEntity<JobOfferDTO> getActiveById(@PathVariable Long id) {
        return ResponseEntity.ok(jobOfferService.findActiveById(id));
    }

    //  2. EMPLEADOR – GESTIÓN DE SUS PROPIAS OFERTAS
    @Operation(
            summary = "Obtener ofertas de un empleador",
            description = "Solo el empleador dueño o un administrador pueden ver estas ofertas."
    )
    @ApiResponse(responseCode = "200", description = "Ofertas obtenidas correctamente")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isEmployerOwner(#employerId)")
    @GetMapping("/employer/{employerId}")
    public ResponseEntity<List<JobOfferDTO>> getByEmployer(@PathVariable Long employerId) {
        return ResponseEntity.ok(jobOfferService.findByEmployer(employerId));
    }

    @Operation(
            summary = "Crear una oferta laboral",
            description = "Disponible para empleadores autenticados o administradores."
    )
    @ApiResponse(responseCode = "201", description = "Oferta creada con éxito")
    @PreAuthorize("hasRole('EMPLEADOR') or hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<JobOfferDTO> create(@Valid @RequestBody JobOfferCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(jobOfferService.create(dto));
    }

    @Operation(
            summary = "Actualizar una oferta laboral",
            description = "Solo el dueño de la oferta o el administrador pueden modificarla."
    )
    @ApiResponse(responseCode = "200", description = "Oferta actualizada correctamente")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isJobOfferOwner(#id)")
    @PutMapping("/{id}")
    public ResponseEntity<JobOfferDTO> update(@PathVariable Long id,
                                              @Valid @RequestBody JobOfferUpdateDTO dto) {
        return ResponseEntity.ok(jobOfferService.update(id, dto));
    }

    @Operation(
            summary = "Eliminar una oferta laboral",
            description = "Solo el empleador dueño o el administrador pueden eliminarla."
    )
    @ApiResponse(responseCode = "204", description = "Oferta eliminada correctamente")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isJobOfferOwner(#id)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        jobOfferService.delete(id);
        return ResponseEntity.noContent().build();
    }


    // 3. ADMIN – CONSULTA TOTAL
    @Operation(
            summary = "Obtener todas las ofertas laborales",
            description = "Solo administradores pueden ver TODAS las ofertas."
    )
    @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<JobOfferDTO>> getAll() {
        return ResponseEntity.ok(jobOfferService.findAll());
    }

    @Operation(
            summary = "Obtener ofertas paginadas",
            description = "Solo administradores pueden paginar la lista completa."
    )
    @ApiResponse(responseCode = "200", description = "Página obtenida correctamente")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/paginated")
    public ResponseEntity<Page<JobOfferDTO>> getAllPaginated(Pageable pageable) {
        return ResponseEntity.ok(jobOfferService.findAll(pageable));
    }

    @Operation(
            summary = "Buscar ofertas por estado",
            description = "Permite a administradores buscar ofertas por estado: ACTIVA, CERRADA, INACTIVA, VENCIDA."
    )
    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<JobOfferDTO>> findByStatus(@PathVariable JobOfferStatus status) {
        return ResponseEntity.ok(jobOfferService.findByStatus(status));
    }


    // PATCH → Cerrar oferta laboral


    @Operation(
            summary = "Cerrar una oferta laboral",
            description = "Cambia el estado de una oferta a CERRADA sin alterar otros campos."
    )
    @PatchMapping("/{id}/close")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isJobOfferOwner(#id)")
    public ResponseEntity<JobOfferDTO> closeOffer(@PathVariable Long id) {

        JobOfferUpdateDTO dto = new JobOfferUpdateDTO(
                null, null, null, null,
                JobOfferStatus.CERRADA
        );

        return ResponseEntity.ok(jobOfferService.update(id, dto));
    }

}



