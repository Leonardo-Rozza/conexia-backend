package com.conexia.utils.mapper;

import com.conexia.persistence.entity.EmployerEntity;
import com.conexia.service.dto.EmployerDTO;
import com.conexia.service.dto.EmployerUpdateDTO;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface EmployerMapper {

    @Mapping(source = "user.id", target = "userId")
    EmployerDTO toDto(EmployerEntity employerEntity);

    @Mapping(source = "userId", target = "user.id")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    EmployerEntity toEntity(EmployerDTO employerDTO);

    // ===== Update parcial (patch) =====
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "idEmployer", ignore = true)
    @Mapping(target = "user", ignore = true) // No cambiar usuario en updates
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDTO(EmployerUpdateDTO dto, @MappingTarget EmployerEntity entity);

    // ===== Creación (forzada sin Id/fechas) =====
    @Mapping(target = "idEmployer", ignore = true)
    @Mapping(source = "userId", target = "user.id")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    EmployerEntity toEntityForCreation(EmployerDTO dto);



}
