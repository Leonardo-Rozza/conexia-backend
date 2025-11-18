package com.conexia.utils.mapper;

import com.conexia.persistence.entity.GraduateEntity;
import com.conexia.service.dto.GraduateDTO;
import com.conexia.service.dto.GraduateUpdateDTO;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface GraduateMapper {

    @Mapping(source = "user.id", target = "userId")
    GraduateDTO toDTO(GraduateEntity graduateEntity);

    @Mapping(source = "userId", target = "user.id")
    @Mapping(target = "idGraduate", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    GraduateEntity toEntity(GraduateDTO graduateDTO);

    // ===== Update parcial (patch) =====
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "idGraduate", ignore = true)
    @Mapping(target = "user", ignore = true) // No cambiar usuario en updates
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDTO(GraduateUpdateDTO dto, @MappingTarget GraduateEntity entity);

    // ===== Creación (forzada sin Id/fechas) =====
    @Mapping(target = "idGraduate", ignore = true)
    @Mapping(source = "userId", target = "user.id")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    GraduateEntity toEntityForCreation(GraduateDTO dto);

}
