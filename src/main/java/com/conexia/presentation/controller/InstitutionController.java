package com.conexia.presentation.controller;

import com.conexia.service.InstitutionService;
import com.conexia.service.dto.InstitutionDTO;
import com.conexia.service.dto.InstitutionUpdateDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@RequestMapping("/api/institutions")
@Tag(name = "Instituciones", description = "Gestión de instituciones educativas")
public class InstitutionController {

    private final InstitutionService  institutionService;

    public InstitutionController(InstitutionService institutionService) {
        this.institutionService = institutionService;
    }

    @Operation(summary = "Obtener todas las instituciones", description = "Lista todas las instituciones registradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    })
    @GetMapping
    public ResponseEntity<List<InstitutionDTO>> getAll(){
        List<InstitutionDTO> dtoList = institutionService.findAll();

        return ResponseEntity.ok(dtoList);
    }

    @Operation(summary = "Obtener instituciones con paginación")
    @ApiResponse(responseCode = "200", description = "Página obtenida correctamente.")
    @GetMapping("/paginated")
    public ResponseEntity<Page<InstitutionDTO>> getAllPaginated(Pageable pageable){
        Page<InstitutionDTO> institutions = institutionService.findAll(pageable);
        return ResponseEntity.ok(institutions);
    }

    @Operation(summary = "Obtener institución por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Institución encontrada"),
            @ApiResponse(responseCode = "404", description = "Institución no encontrada")
    })
    @PreAuthorize("hasRole('ADMIN') or @securityService.isInstitutionOwner(#id)")
    @GetMapping("/{id}")
    public ResponseEntity<InstitutionDTO> getById(@PathVariable Long id){
        return ResponseEntity.ok(institutionService.findById(id));
    }

    @Operation(summary = "Crear nueva institución")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Institución creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o email duplicado")
    })
    @PostMapping
    public ResponseEntity<InstitutionDTO> save(@Valid @RequestBody InstitutionDTO institutionDTO){
        InstitutionDTO institution = institutionService.save(institutionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(institution);
    }

    @Operation(summary = "Actualizar institución")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Institución actualizada correctamente"),
            @ApiResponse(responseCode = "404", description = "Institución no encontrada"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o email duplicado")
    })
    @PreAuthorize("hasRole('ADMIN') or @securityService.isInstitutionOwner(#id)")
    @PutMapping("/{id}")
    public ResponseEntity<InstitutionDTO> update(@PathVariable Long id,
                                                 @Valid @RequestBody InstitutionUpdateDTO institutionUpdateDTO){

        InstitutionDTO institution = institutionService.update(id, institutionUpdateDTO);
        return ResponseEntity.ok(institution);
    }

    @Operation(summary = "Eliminar institución")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Institución eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Institución no encontrada")
    })
    @PreAuthorize("hasRole('ADMIN') or @securityService.isInstitutionOwner(#id)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        institutionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
