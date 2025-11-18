package com.conexia.presentation.controller;

import com.conexia.service.CourseService;
import com.conexia.service.dto.CourseCreateDTO;
import com.conexia.service.dto.CourseDTO;
import com.conexia.service.dto.CourseUpdateDTO;
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
@RequestMapping("/api/courses")
@Tag(name = "Cursos", description = "Gestión de Cursos")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @Operation(summary = "Obtener todos los cursos", description = "Lista de todos los cursos.")
    @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente.")
    @GetMapping
    public ResponseEntity<List<CourseDTO>> getAll() {
        List<CourseDTO> courseDTOS = this.courseService.findAll();
        return ResponseEntity.ok(courseDTOS);
    }


    @Operation(summary = "Obtener todos los cursos con paginación")
    @ApiResponse(responseCode = "200", description = "Pagina obtenida correctamente.")
    @GetMapping("/paginated")
    public ResponseEntity<Page<CourseDTO>> getAllPaginated(Pageable pageable) {
        Page<CourseDTO> courseDTOS = this.courseService.findAll(pageable);
        return ResponseEntity.ok(courseDTOS);
    }

    @Operation(summary = "Obtener un curso por ID")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Obtenido correctamente"),
                    @ApiResponse(responseCode = "404", description = "Curso no encontrado.")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<CourseDTO> getById(@PathVariable Long id){
        return ResponseEntity.ok(this.courseService.findById(id));
    }

    @Operation(summary = "Crear un Curso")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "Creado con éxito."),
                    @ApiResponse(responseCode = "400", description = "Datos inválidos.")
            }
    )
    @PreAuthorize("hasRole('ADMIN') or @securityService.isInstitutionOwner(#courseCreateDTO.idInstitution())")
    @PostMapping
    public ResponseEntity<CourseDTO> save(@Valid @RequestBody CourseCreateDTO courseCreateDTO){
        CourseDTO courseDTO = this.courseService.save(courseCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(courseDTO);
    }

    @Operation(summary = "Actualizar un curso")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Actualizado correctamente."),
                    @ApiResponse(responseCode = "404", description = "Curso no encontrado."),
                    @ApiResponse(responseCode = "400", description = "Datos inválidos.")
            }
    )
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCourseOwner(#id)")
    @PutMapping("/{id}")
    public ResponseEntity<CourseDTO> update(@PathVariable Long id, @Valid @RequestBody CourseUpdateDTO courseUpdateDTO){
        CourseDTO courseDTO = this.courseService.update(id, courseUpdateDTO);
        return ResponseEntity.ok(courseDTO);
    }

    @Operation(summary = "Eliminar un curso por ID.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "Curso eliminado correctamente."),
                    @ApiResponse(responseCode = "404", description = "Curso no encontrado.")
            }
    )
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCourseOwner(#id)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        this.courseService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
