package src.spring.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import src.spring.database.repository.CompanyRepository;
import src.spring.dto.CompanyReadDto;
import src.spring.database.entity.Company;
import src.spring.listener.entity.EntityEvent;

import java.util.Collections;
import java.util.Optional;



@ExtendWith(
        MockitoExtension.class
)
class CompanyServiceTest {

    private static final Integer COMPANY_ID = 1;

    @Mock
    private CompanyRepository companyRepository;
    @Mock
    private UserService userService;
    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private CompanyService companyService;

    @Test
    void findById() {
        //1
        Mockito.doReturn(Optional.of(new Company(COMPANY_ID, null, Collections.emptyMap())))
                .when(companyRepository)
                .findById(COMPANY_ID);
        //2
        var actualResult = companyService.findById(COMPANY_ID);
        //3
        //проверили существует ли вообще
        Assertions.assertTrue(actualResult.isPresent());

        var expectedResult = new CompanyReadDto(COMPANY_ID);
        //Если существует, то
        actualResult.ifPresent(actual -> Assertions.assertEquals(expectedResult, actual));

        //нужно удостовериться, что был вызван мок eventPublisher
        Mockito.verify(eventPublisher).publishEvent(Mockito.any(EntityEvent.class));

        /*
        проверяет, что во время теста ни один из указанных фиктивных
        объектов не участвовал в каких-либо взаимодействиях
        Иначе кидает исключение
         */
        Mockito.verifyNoInteractions(userService);
    }
}
