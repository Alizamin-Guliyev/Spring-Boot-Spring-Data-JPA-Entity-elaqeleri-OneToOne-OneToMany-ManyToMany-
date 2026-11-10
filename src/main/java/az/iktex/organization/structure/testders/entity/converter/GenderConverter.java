package az.iktex.organization.structure.testders.entity.converter;

import az.iktex.organization.structure.testders.enums.Gender;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class GenderConverter implements AttributeConverter<Gender, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Gender gender) {
        return gender == null ? null : gender.getDbCode();
    }

    @Override
    public Gender convertToEntityAttribute(Integer dbCode) {
        return Gender.findByDbCode(dbCode);
    }
}
