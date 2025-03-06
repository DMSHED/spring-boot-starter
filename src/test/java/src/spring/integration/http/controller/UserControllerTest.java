package src.spring.integration.http.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import src.spring.database.entity.Role;
import src.spring.dto.UserCreateEditDto;
import src.spring.integration.IntegrationTestBase;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static src.spring.dto.UserCreateEditDto.Fields.*;

@RequiredArgsConstructor
@AutoConfigureMockMvc//специальный обьект, для имитации запросов в приложении
//это предпочтительный, декларативный вариант создания тестовой сущности
@WithMockUser(
        username = "test@gmail.com",
        password = "test",
        authorities = {"ADMIN", "USER"}
) //ВТОРОЙ ВАРИАНТ создания тестовой сущности, можно и над отдельным методом ставить
class UserControllerTest extends IntegrationTestBase {

    private final MockMvc mockMvc;

    //чтобы тесты работали нужно добавить сущность, которая будет в контексте на время тестов
    @BeforeEach
    void init(){
        //ПЕРВЫЙ ВАРИАНТ создания тестовой сущности

//        List<GrantedAuthority> roles = Arrays.asList(Role.ADMIN, Role.USER);
//        User testUser = new User("test@gmail.com","test", roles);
//
//        TestingAuthenticationToken testingAuthenticationToken = new TestingAuthenticationToken(testUser, testUser.getPassword(), roles);
//
//        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
//        securityContext.setAuthentication(testingAuthenticationToken);
//
//        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void findAll() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("user/users"))
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attribute("users", hasSize(5)));
    }

    @Test
    void create() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .param(username, "test")
                .param(firstname, "test")
                .param(lastname, "test")
                .param(role, "ADMIN")
                .param(companyId, "1")
                .param(birthDate, "2000-01-01")
        )
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrlPattern("/users/{\\d+}")
                );
    }

}