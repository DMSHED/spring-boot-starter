package src.spring.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import src.spring.database.entity.Company;
import src.spring.database.entity.User;
import src.spring.database.repository.CompanyRepository;
import src.spring.dto.UserCreateEditDto;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserCreareEditMapper implements Mapper<UserCreateEditDto, User>{

    private final CompanyRepository companyRepository;

    @Override
    public User map(UserCreateEditDto fromObject, User toObject) {
        copy(fromObject, toObject);
        return toObject;
    }

    @Override
    public User map(UserCreateEditDto object) {
        User user = new User();
        copy(object, user);
        return user;
    }

    private void copy(UserCreateEditDto object, User user) {
        user.setUsername(object.getUsername());
        user.setBirthDate(object.getBirthDate());
        user.setFirstName(object.getFirstname());
        user.setLastName(object.getLastname());
        user.setRole(object.getRole());
        user.setCompany(getCompany(object.getCompanyId()));
    }



    public Company getCompany(Integer companyId) {
        return Optional.ofNullable(companyId)
                .flatMap(companyRepository::findById)
                .orElse(null);
    }

}
