package com.conexia.utils.mapper;

import com.conexia.persistence.entity.CourseEntity;
import com.conexia.service.dto.CourseCreateDTO;
import com.conexia.service.dto.CourseDTO;
import com.conexia.service.dto.CourseUpdateDTO;
import org.mapstruct.*;


@Mapper(
        componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface CourseMapper {

    @Mapping(source = "institution.idInstitucion", target = "idInstitution")
    CourseDTO toDTO(CourseEntity courseEntity);

    @Mapping(source = "idInstitution", target = "institution.idInstitucion")
    @Mapping(target = "idCourse", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    CourseEntity toEntityForCreation(CourseCreateDTO courseCreateDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "idCourse", ignore = true)
    @Mapping(target = "institution", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDTO(CourseUpdateDTO dto, @MappingTarget CourseEntity entity);
}

