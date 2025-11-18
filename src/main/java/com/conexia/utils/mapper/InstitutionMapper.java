package com.conexia.utils.mapper;

import com.conexia.persistence.entity.InstitutionEntity;
import com.conexia.persistence.entity.UserEntity;
import com.conexia.service.dto.InstitutionDTO;
import com.conexia.service.dto.InstitutionUpdateDTO;
import org.mapstruct.*;
import java.util.List;

@Mapper(
    componentModel = "spring",
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface InstitutionMapper {

    // ===== Entity -> DTO =====
    @Mapping(source = "user.id", target = "userId")
    InstitutionDTO toDTO(InstitutionEntity entity);

    // ===== DTO -> Entity =====
    @Mapping(source = "userId", target = "user.id")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    InstitutionEntity toEntity(InstitutionDTO dto);

    // ===== Update parcial (patch) =====
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "idInstitucion", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDTO(InstitutionUpdateDTO dto, @MappingTarget InstitutionEntity entity);

    // ===== Creación (forzada sin Id/fechas) =====
    @Mapping(target = "idInstitucion", ignore = true)
    @Mapping(source = "userId", target = "user.id")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    InstitutionEntity toEntityForCreation(InstitutionDTO dto);

}
