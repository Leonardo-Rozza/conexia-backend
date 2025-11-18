package com.conexia.utils.mapper;

import com.conexia.persistence.entity.JobOfferEntity;
import com.conexia.service.dto.JobOfferDTO;
import com.conexia.service.dto.JobOfferCreateDTO;
import com.conexia.service.dto.JobOfferUpdateDTO;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface JobOfferMapper {

    @Mapping(source = "employer.idEmployer", target = "employerId")
    JobOfferDTO toDTO(JobOfferEntity entity);

    @Mapping(target = "idOffer", ignore = true)
    @Mapping(target = "employer", ignore = true)
    @Mapping(target = "publicationDate", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    JobOfferEntity toEntityForCreation(JobOfferCreateDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "idOffer", ignore = true)
    @Mapping(target = "employer", ignore = true)
    @Mapping(target = "publicationDate", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDTO(JobOfferUpdateDTO dto, @MappingTarget JobOfferEntity entity);
}
