package src.spring.database.repository;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.history.RevisionRepository;
import src.spring.database.entity.Role;
import src.spring.database.entity.User;
import src.spring.dto.PersonalInfo2;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


//@Repository //можно убрать, т.к делаем extends от JpaRepository
public interface UserRepository extends
        JpaRepository<User, Long>, // для подключения спринговых запросов
        FilterUserRepository, // для подключения собственного запроса
        RevisionRepository<User, Long, Integer> //для того, чтобы обращаться к таблице, в которой хранятся изменения User
{


    //указывать % можно в HQL
    @Query(value = "select u from User u " +
                   "where u.firstName like %:firstname% and u.lastName like %:lastname%")
    List<User> findAllBy(String firstname, String lastname);

    /*
    Изначально Query только для селектов, но мы можем выполнить
    любой запрос, если добавим Аннотацию @Modifying
     */

    /*
    clear - чистит контекст, после выполнения запроса,
    уберет все данные, затем можем получить новые, уже обновленные
    flush - нужен, чтобы перед очисткой, данные сохранились в бд
    иначе они потеряются после очистки контекста
     сначала идет flush
     потом наш запрос на update
     потом уже очистка контекста после update
     */
    //HQL
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update User u " +
           "set u.role = :role " +
           "where u.id in (:ids)")
    int updateRole(Role role, Long... ids);

    //SQL
//    @Modifying(clearAutomatically = true, flushAutomatically = true)
//    @Query("UPDATE User u " +
//           "set u.role = :role " +
//           "where u.id in (:ids)")
//    int updateRole(Role role, Long... ids);

    //первого пользователя с конца
    Optional<User> findFirstByOrderByIdDesc();

    /*
    оптимистические или пессимистические блокировки строк
    оптимистические на уровне приложения использует поле version с аннотацией @Version
    пессимистические на уровне БД

    Fetch size — это свойство, которое контролирует количество строк,
    извлекаемых из базы данных за один раз
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints(@QueryHint(name = "org.hibernate.fetchSize", value = "50"))//дополнительные оптимизации, которые хотим применить к запросу
    List<User> findFirst3ByBirthDateBefore(LocalDate birthDate, Sort sort);

    //Collection, Stream
    // Slice наследуется от Streamable(аналог стрима, обертка на Итератором), есть методы
    // Page наследуется от Slice
    @EntityGraph(attributePaths = {"company"})// активируем EntityGraph,
    @Query(value = "select u from User u",
    countQuery = "select count(distinct u.firstName) from User u") //изменили дефолтное поведение
    Page<User> findAllBy(Pageable pageable);

    /*
    можно делать join fetch в запросе на компанию, чтобы вместе с юзером
    компанию доставать, а не отдельными запросами,
    а можно сделать @NamedEntityGraph и здесь активировать в параметре
    value у @EntityGraph, указав название - громоздко
    можно в attributePaths задать

    лимит и оффсеты не отрабатывают, когда достаем еще и локали, т.к
    для каждого юзера сразу выводится 2 локали, (декартовое локалей и юзеров)

     */
//    @Query(value = "select u.firstname, u.lastname, u.birth_date from public.users u " +
//                   "where u.company_id = :companyId", nativeQuery = true)
//    List<PersonalInfo> findAllByCompanyId(Integer company_id);
//    <T> List<T> findAllByCompanyId(Integer company_id, Class<T> clazz);
    //чтобы поля таблицы и DTO совпали, можно давать алиас
    @Query(nativeQuery = true, value = "select firstname," +
                                       "lastname," +
                                       "birth_date birthDate " +
                                       "from users " +
                                       "where company_id = :companyId")
    List<PersonalInfo2> findAllByCompanyId(Integer companyId);



}
