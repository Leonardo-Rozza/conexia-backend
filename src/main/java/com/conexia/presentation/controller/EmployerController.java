package com.conexia.presentation.controller;

import com.conexia.service.EmployerService;
import com.conexia.service.dto.EmployerDTO;
import com.conexia.service.dto.EmployerUpdateDTO;
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
@RequestMapping("/api/employers")
@Tag(name = "Empleadores", description = "Gestión de Empleadores")
public class EmployerController {

    private final EmployerService employerService;

    public EmployerController(EmployerService employerService) {
        this.employerService = employerService;
    }

    @Operation(summary = "Obtener todos los empleadores", description = "Lista de todos los empleadores registrados")
    @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente.")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<EmployerDTO>> getAll(){
        List<EmployerDTO> dtoList = this.employerService.findAll();
        return ResponseEntity.ok(dtoList);
    }

    @Operation(summary = "Obtener empleadores con paginación")
    @ApiResponse(responseCode = "200", description = "Página obtenida correctamente.")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/paginated")
    public ResponseEntity<Page<EmployerDTO>> getAllPaginated(Pageable pageable) {
        Page<EmployerDTO> employerDTOPage = this.employerService.findAll(pageable);
        return ResponseEntity.ok(employerDTOPage);
    }

    @Operation(summary = "Obtener empleador por ID")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Empleador obtenido con éxito."),
                    @ApiResponse(responseCode = "404", description = "Empleador no encontrado."),
            }
    )
    @PreAuthorize("hasRole('ADMIN') or @securityService.isEmployerOwner(#id)")
    @GetMapping("/{id}")
    public ResponseEntity<EmployerDTO> getById(@PathVariable Long id){
        return ResponseEntity.ok(this.employerService.findById(id));
    }

    @Operation(summary = "Crear un empleador")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "Empleador creado con éxito."),
                    @ApiResponse(responseCode = "400", description = "Datos inválidos o email duplicado.")
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<EmployerDTO> save(@Valid @RequestBody EmployerDTO employerDTO){
        EmployerDTO employer = this.employerService.save(employerDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(employer);
    }

    @Operation(summary = "Actualizar un empleador")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Empleador actualizado correctamente"),
                    @ApiResponse(responseCode = "404", description = "Empleador no encontrado"),
                    @ApiResponse(responseCode = "400", description = "Datos inválidos o email duplicado")
            }
    )
    @PreAuthorize("hasRole('ADMIN') or @securityService.isEmployerOwner(#id)")
    @PutMapping("/{id}")
    public ResponseEntity<EmployerDTO> update(@PathVariable Long id, @Valid @RequestBody EmployerUpdateDTO employerUpdateDTO){
        EmployerDTO employer = this.employerService.update(id, employerUpdateDTO);
        return ResponseEntity.ok(employer);
    }

    @Operation(summary = "Eliminar un Empleador por ID.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "Empleador eliminado correctamente."),
                    @ApiResponse(responseCode = "404", description = "Empleador no encontrado."),
            }
    )
    @PreAuthorize("hasRole('ADMIN') or @securityService.isEmployerOwner(#id)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        this.employerService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
