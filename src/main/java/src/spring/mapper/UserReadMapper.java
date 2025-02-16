package src.spring.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import src.spring.database.entity.User;
import src.spring.dto.CompanyReadDto;
import src.spring.dto.UserReadDto;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserReadMapper implements Mapper<User, UserReadDto> {

    private final CompanyReadMapper companyReadMapper = new CompanyReadMapper();

    @Override
    public UserReadDto map(User object) {
        CompanyReadDto companyReadDto = Optional.ofNullable(object.getCompany())
                .map(companyReadMapper::map)
                .orElse(null);

        return new UserReadDto(
                object.getId(),
                object.getUsername(),
                object.getBirthDate(),
                object.getFirstName(),
                object.getLastName(),
                object.getRole(),
                companyReadDto

        );
    }
}
