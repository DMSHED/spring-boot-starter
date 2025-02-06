package src.spring.integration;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import src.spring.database.pool.ConnectionPool;

@TestConfiguration
public class TestApplicationRunner {

    //будем подменять бин на мок, переопределили в аннотации @SpringBootTest
    @MockitoBean(name = "pool1")
    private ConnectionPool pool1;
}
