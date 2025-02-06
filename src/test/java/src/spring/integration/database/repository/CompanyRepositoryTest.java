package src.spring.integration.database.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import src.spring.database.entity.Company;
import src.spring.database.repository.CompanyRepository;
import src.spring.integration.annotation.IT;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@IT
@RequiredArgsConstructor
//@Transactional //будет создавать транзакции и закрывать в каждом методе, можно над отдельным методом
//в конце будет завершать транзакцию роллбэком, по дефолту, есть аннотации, которые делает коммит
//@Commit // теперь транзакции будут завершаться коммитом, можно ставить над отдельным методом/тестом
class CompanyRepositoryTest {

    private final Integer SOME_ID = 5;

    private final EntityManager entityManager;

    //управляем транзакциями в ручную
    private final TransactionTemplate transactionTemplate;

    private final CompanyRepository companyRepository;

    @Test
    void checkFindByQueries() {
        Optional<Company> google = companyRepository.findByName("google");
        List<Company> companies = companyRepository.findAllByNameContainingIgnoreCase("a");

    }

    @Test
    void delete() {
        Optional<Company> maybeCompany = companyRepository.findById(SOME_ID);

        assertTrue(maybeCompany.isPresent());
        maybeCompany.ifPresent(companyRepository::delete);

        //нужен flush т.к операция delete не выполнится, пока явно не будет указания
        //это либо коммит в конце транзакции, ну или flush
        //но т.к мы не указывали @Commit, то в конце роллбэк откатит
        //изменений в бд не будет
        entityManager.flush();

        assertTrue(companyRepository.findById(SOME_ID).isEmpty());
    }



    @Test
    void findById(){
        transactionTemplate.executeWithoutResult(transaction -> {
            var company = entityManager.find(Company.class, 1L);
            assertNotNull(company);
            Assertions.assertThat(company.getLocales()).hasSize(2);
        });
    }


    @Test
    @Commit
    void save(){
        var company = Company.builder()
                .name("TEST2")
                .locales(Map.of(
                        "ru", "Apple описание",
                        "en", "Apple description"
                ))
                .build();

        entityManager.persist(company);

        assertNotNull(company.getId());
    }
}