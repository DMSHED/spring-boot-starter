package src.spring.integration.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import src.spring.config.DatabaseProperties;
import src.spring.dto.CompanyReadDto;
import src.spring.integration.annotation.IT;
import src.spring.service.CompanyService;

/*
SpringExtension нужен для интеграции фреймворка
Spring TestContext в модель программирования JUnit 5 Jupiter

Он настраивает контекст приложения Spring, управляет
зависимостями и транзакционным поведением во время тестов
 */
//@ExtendWith(
//        SpringExtension.class
//)
//@ContextConfiguration(
//        classes = ApplicationRunner.class, //загружает класс конфигурации
//        initializers = ConfigDataApplicationContextInitializer.class //нужен для инициализации ApplicationContext,загружает .properties
//)



//@SpringBootTest //Аналог тому что выше написали
//@ActiveProfiles("test")
@IT
@RequiredArgsConstructor
//@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)// убрали здесь и установили в spring.properties
public class CompanyServiceIT {

    private static final Integer COMPANY_ID = 1;

    private final CompanyService companyService;

    private final DatabaseProperties databaseProperties;

    @Test
    void findById(){
        var actualResult = companyService.findById(COMPANY_ID);
        //3
        //проверили существует ли вообще
        Assertions.assertTrue(actualResult.isPresent());

        var expectedResult = new CompanyReadDto(COMPANY_ID);
        //Если существует, то
        actualResult.ifPresent(actual -> Assertions.assertEquals(expectedResult, actual));

    }
}
