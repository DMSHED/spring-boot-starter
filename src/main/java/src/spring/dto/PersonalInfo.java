package src.spring.dto;

import java.time.LocalDate;

public record PersonalInfo(
        String firstName,
        String lastName,
        LocalDate birthday
) {
}
