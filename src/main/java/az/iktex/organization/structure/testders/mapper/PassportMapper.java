package az.iktex.organization.structure.testders.mapper;

import az.iktex.organization.structure.testders.entity.Passport;
import az.iktex.organization.structure.testders.request.PassportRequest;
import az.iktex.organization.structure.testders.response.PassportResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PassportMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    Passport toEntity(PassportRequest request);

    PassportResponse toResponse(Passport passport);

    List<PassportResponse> toResponseList(List<Passport> passports);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    //@MappingTarget bildirir ki, yeni Entity yaratma, mövcud Passport obyektini yenilə.
    void updateEntity(PassportRequest request, @MappingTarget Passport passport);
}
