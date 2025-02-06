package src.spring.integration.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import src.spring.database.pool.ConnectionPool;
import src.spring.integration.annotation.IT;
import src.spring.service.UserService;

@IT
@RequiredArgsConstructor
public class UserServiceIT {
    private final UserService userService;

    private ConnectionPool pool1;

    @Test
    void test(){
        System.out.println();
    }
}
