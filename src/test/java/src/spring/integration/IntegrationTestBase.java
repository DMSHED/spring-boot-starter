package src.spring.integration;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import src.spring.integration.annotation.IT;

//нужен для того, чтобы поднимать и запускать контейнер с бд перед тестированием
//должен наследоваться интеграционными тестами
@IT
@Sql({
        "classpath:sql/data.sql"
}) // заполнит бд тестовыми данными
public abstract class IntegrationTestBase {

    //указываем в параметрах docker image
    private static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:latest");

    @BeforeAll
    static void runContainer() {
        container.start();
    }

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
    }
}
