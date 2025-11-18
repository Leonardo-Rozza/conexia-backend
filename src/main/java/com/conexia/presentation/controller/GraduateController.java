package com.conexia.presentation.controller;

import com.conexia.service.GraduateService;
import com.conexia.service.dto.GraduateDTO;
import com.conexia.service.dto.GraduateUpdateDTO;
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
@RequestMapping("/api/graduates")
@Tag(name = "Graduados", description = "Gestion de Graduados")
public class GraduateController {

    private final GraduateService graduateService;

    public GraduateController(GraduateService graduateService) {
        this.graduateService = graduateService;
    }

    @Operation(summary = "Obtener todos los graduados", description = "Lista de todos los graduados registrados.")
    @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente.")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<GraduateDTO>> getAll(){
        List<GraduateDTO> graduateDTOS = this.graduateService.findAll();

        return ResponseEntity.ok(graduateDTOS);
    }

    @Operation(summary = "Obtener los graduados con paginación", description = "Lista de todos los graduados registrados.")
    @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente.")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/paginated")
    public ResponseEntity<Page<GraduateDTO>> getAllPaginated(Pageable pageable){
        Page<GraduateDTO> graduateDTOPage = this.graduateService.findAll(pageable);
        return ResponseEntity.ok(graduateDTOPage);
    }

    @Operation(summary = "Obtener Graduado por ID.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Obtenido con éxito."),
                    @ApiResponse(responseCode = "404", description = "Graduado no encontrado.")
            }
    )
    @PreAuthorize("hasRole('ADMIN') or @securityService.isGraduateOwner(#id)")
    @GetMapping("/{id}")
    public ResponseEntity<GraduateDTO> getById(@PathVariable Long id){
        return ResponseEntity.ok(this.graduateService.findById(id));
    }

    @Operation(summary = "Crear un graduado")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "Graduado creado con éxito."),
                    @ApiResponse(responseCode = "400", description = "Datos inválidos o email duplicado.")
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<GraduateDTO> save(@Valid @RequestBody GraduateDTO graduateDTO){
        GraduateDTO graduate = this.graduateService.save(graduateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(graduate);
    }

    @Operation(summary = "Actualizar un Graduado")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Graduado actualizado con éxito."),
                    @ApiResponse(responseCode = "404", description = "Graduado no encontrado."),
                    @ApiResponse(responseCode = "400", description = "Datos inválidos o email duplicado.")
            }
    )
    @PreAuthorize("hasRole('ADMIN') or @securityService.isGraduateOwner(#id)")
    @PutMapping("/{id}")
    public ResponseEntity<GraduateDTO> update(@PathVariable Long id, @Valid @RequestBody GraduateUpdateDTO graduateUpdateDTO){
        GraduateDTO graduate = this.graduateService.update(id, graduateUpdateDTO);
        return ResponseEntity.ok(graduate);
    }

    @Operation(summary = "Eliminar un Graduado")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "Graduado eliminado con éxito."),
                    @ApiResponse(responseCode = "404", description = "Graduado no encontrado."),
            }
    )
    @PreAuthorize("hasRole('ADMIN') or @securityService.isGraduateOwner(#id)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        this.graduateService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
