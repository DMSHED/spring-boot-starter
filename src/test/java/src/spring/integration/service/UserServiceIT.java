package src.spring.integration.service;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import src.spring.database.entity.Role;
import src.spring.database.pool.ConnectionPool;
import src.spring.dto.UserCreateEditDto;
import src.spring.dto.UserReadDto;
import src.spring.integration.IntegrationTestBase;
import src.spring.integration.annotation.IT;
import src.spring.service.UserService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
public class UserServiceIT extends IntegrationTestBase {

    private static final Long USER_ID = 1L;
    private static final Integer COMPANY_ID = 1;

    private final UserService userService;

    @Test
    void findAll(){
        List<UserReadDto> result = userService.findAll();
        Assertions.assertThat(result).hasSize(5);
    }

    @Test
    void findById(){
        Optional<UserReadDto> maybeUser = userService.findById(USER_ID);
        assertTrue(maybeUser.isPresent());

        maybeUser.ifPresent(user -> {
            assertEquals("ivan@gmail.com", user.getUsername());
        });
    }

    @Test
    void create() {
        UserCreateEditDto userDto = new UserCreateEditDto(
                "test",
                "test",
                LocalDate.now(),
                "test",
                "test",
                Role.ADMIN,
                COMPANY_ID,
                new MockMultipartFile("test", new byte[0])
        );
        UserReadDto actualResult = userService.create(userDto);

        assertEquals(userDto.getUsername() ,actualResult.getUsername());
    }

    @Test
    void update() {
        UserCreateEditDto userDto = new UserCreateEditDto(
                "test2",
                "test",
                LocalDate.now(),
                "test3",
                "test",
                Role.ADMIN,
                COMPANY_ID,
                new MockMultipartFile("test", new byte[0])
        );

        Optional<UserReadDto> actualResult = userService.update(USER_ID, userDto);

        assertTrue(actualResult.isPresent());

        actualResult.ifPresent(actual -> {
            assertEquals(userDto.getUsername(), actual.getUsername());
        });
    }

    @Test
    void delete() {
        boolean resultTrue = userService.delete(USER_ID);
        boolean resultFalse = userService.delete(123124L);

        assertTrue(resultTrue);
        assertFalse(resultFalse);
    }
}
