package src.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import src.spring.database.repository.CompanyRepository;
import src.spring.database.repository.UserRepository;
import src.spring.database.entity.Company;

@Service
@RequiredArgsConstructor
public class UserService {

    @Qualifier("userRepository")
    private final UserRepository userReposipory;
    //у нас возвращается не CompanyRepository, а Proxy, поэтому меняем на интерфейс
    private final CompanyRepository companyRepository;


}
