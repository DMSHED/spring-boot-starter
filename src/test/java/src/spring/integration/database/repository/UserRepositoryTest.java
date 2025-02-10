package src.spring.integration.database.repository;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.*;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.jdbc.Sql;
import src.spring.database.entity.Role;
import src.spring.database.entity.User;
import src.spring.database.repository.UserRepository;
import src.spring.dto.PersonalInfo;
import src.spring.dto.UserFilter;
import src.spring.integration.IntegrationTestBase;
import src.spring.integration.annotation.IT;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

//@IT
@RequiredArgsConstructor
public class UserRepositoryTest extends IntegrationTestBase{

    private final UserRepository userRepository;

    @Test
    void checkBatch(){
        var users = userRepository.findAll();

        userRepository.updateCompanyAndRole(users);
        System.out.println();
    }

    @Test
    void checkJdbcTemplate(){
        var users = userRepository.findAllByCompanyIdandRole(1, Role.USER);
        System.out.println();
    }

    @Test
    @Commit
    void checkAuditing() {
        var user = userRepository.findById(1L).get();
        user.setBirthday(user.getBirthday().plusYears(1L));

        userRepository.flush();
        System.out.println();

    }

    @Test
    void checkCustomImplementation() {
        UserFilter filter = new UserFilter(
                null, "%ov%", LocalDate.now()
        );

        List<User> users = userRepository.findAllByFilter(filter);
        System.out.printf("");
    }


    /*
    Проекции в Java - это dto
     */
    @Test
    void checkProjections() {
        var check = userRepository.findAllByCompanyId(1);
        System.out.println();
    }

    
    @Test
    void checkPageable() {
        //чтобы задавать лимит и оффсет, включает в себя сортировку
        //работает со всеми видами запросов
        // с pageable появляются еще 2 возвращаемых Коллекции
        PageRequest pageable = PageRequest.of(1, 2, Sort.by("id"));

        var page = userRepository.findAllBy(pageable);

        page.forEach(user -> System.out.println(user.getCompany().getName()));

        //можем легко получить следующую страничку
        while (page.hasNext()) {
            //просто меняется страничка pageNumber
            page = userRepository.findAllBy(page.nextPageable());
            page.forEach(user -> System.out.println(user.getCompany().getName()));
        }

        /*
        Slice все же редко используют, чаще используют Page,
        для пагинации
        в Page можно отобразить количество страниц
        в Slice так нельзя сделать

        Page наследуется от Slice

        Page делает дополнительный запрос на count
        его можно переписать в @Query
        изменить дефолтное поведение этого счетчика
         */


//        Assertions.assertThat(result).hasSize(2);
    }

    //специальные параметры сортировки, чтобы название метода не было километровым
    @Test
    void checkSortById() {
        var sortById = Sort.by("id");
        
        var sortBy = Sort.sort(User.class);
        sortBy.by(User::getFirstName).and(sortBy.by(User::getLastName));

    }

    @Test
    void checkFirstByOrderIdDesc() {
        var allUsers = userRepository.findFirst3ByBirthdayBefore(LocalDate.now(), Sort.by("id").descending());
        Assertions.assertThat(allUsers).hasSize(3);

        var firstUser = userRepository.findFirstByOrderByIdDesc();

        assertTrue(firstUser.isPresent());
        firstUser.ifPresent(user -> assertEquals(5L, user.getId()));
    }

    @Test
    void checkUpdate() {
        Optional<User> ivan = userRepository.findById(1L);
        ivan.ifPresent(user -> assertSame(Role.ADMIN, user.getRole()));
        /*
        Вот этого первого ивана уже нельзя использовать, т.к. мы убрали
        его из контекста с помощью clearautomatically в userRepository
        он был актуален до запроса с обновлением
         */

        var resultCount = userRepository.updateRole(Role.USER, 1L, 5L);
        assertEquals(2, resultCount);

        Optional<User> ivan2 = userRepository.findById(1L);
        ivan.ifPresent(user -> assertSame(Role.USER, user.getRole()));
    }

    @Test
    void checkQueries() {
        var users = userRepository.findAllBy("a", "ov");
        Assertions.assertThat(users).hasSize(3);
    }
}
