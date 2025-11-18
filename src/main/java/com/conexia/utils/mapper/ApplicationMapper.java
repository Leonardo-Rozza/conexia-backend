package com.conexia.utils.mapper;

import com.conexia.persistence.entity.ApplicationEntity;
import com.conexia.service.dto.ApplicationDTO;
import com.conexia.service.dto.ApplicationCreateDTO;
import com.conexia.service.dto.ApplicationUpdateDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface ApplicationMapper {

    @Mapping(source = "graduate.idGraduate", target = "graduateId")
    @Mapping(source = "jobOffer.idOffer", target = "offerId")
    ApplicationDTO toDTO(ApplicationEntity entity);

    @Mapping(target = "idApplication", ignore = true)
    @Mapping(source = "graduateId", target = "graduate.idGraduate")
    @Mapping(source = "offerId", target = "jobOffer.idOffer")
    @Mapping(target = "status", ignore = true)     // se completa en service
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    ApplicationEntity toEntityForCreation(ApplicationCreateDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(ApplicationUpdateDTO dto, @MappingTarget ApplicationEntity entity);
}

