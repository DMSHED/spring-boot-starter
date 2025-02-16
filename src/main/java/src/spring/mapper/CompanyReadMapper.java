package src.spring.mapper;

import org.springframework.stereotype.Component;
import src.spring.database.entity.Company;
import src.spring.dto.CompanyReadDto;

@Component
public class CompanyReadMapper implements Mapper<Company, CompanyReadDto>{
    @Override
    public CompanyReadDto map(Company object) {
        return new CompanyReadDto(
            object.getId(),
            object.getName()
        );
    }
}
