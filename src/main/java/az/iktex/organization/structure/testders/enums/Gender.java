package az.iktex.organization.structure.testders.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum Gender {

    MALE(1),
    FEMALE(2);

    private final Integer dbCode;

    public static Gender findByDbCode(Integer dbCode) {
        if (dbCode == null) {
            return null;
        }
        return Arrays.stream(values())
                .filter(gender -> gender.dbCode.equals(dbCode))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown gender dbCode: " + dbCode));
    }
}
