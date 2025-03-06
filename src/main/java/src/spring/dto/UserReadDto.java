package src.spring.dto;


import lombok.Value;
import src.spring.database.entity.Role;

import java.time.LocalDate;
import java.util.Objects;

@Value
public class UserReadDto {
    Long id;
    String username;
    LocalDate birthDate;
    String firstname;
    String lastname;
    Role role;
    CompanyReadDto company;

}
