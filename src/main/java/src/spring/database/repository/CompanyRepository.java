package src.spring.database.repository;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.NamedQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import src.spring.bpp.Auditing;
import src.spring.bpp.Transaction;
import src.spring.database.pool.ConnectionPool;
import src.spring.database.entity.Company;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

//также можем пользоваться всеми вариантами создания запросов, как hibernate

//ВТОРОЙ ВАРИАНТ, СОЗДАНИЕ NamedQuery, ставится над сущностью
public interface CompanyRepository extends JpaRepository<Company, Integer> {

    /*
    Спринг автоматически будет создавать нам запросы, на основе названия метода
    также он подсказывает, когда пишешь название метода
    если сделать ошибку в названии метода и спринг не сможет его распарсить
    то вылетит ошибка
    можно также вставлять AND или OR между условиями
    есть документация, где можно посмотреть как составлять такие названия
     */


    //ПЕРВЫЙ ВАРИАНТ СОЗДАНИЯ ЗАПРОСОВ, ИМЕНОВАНИЕ МЕТОДОВ
    //если создаем NamedQuery над сущностью, с таким же названием
    //то она имеет приоритет и нужно название аргументов указывать такое же
    // как и параметры в запросе в NamedQuery

    //можем возвращать не только Optional, но и Entity, Future
//    Optional<Company> findByName(@Param("name") String name);

    //Containing - это аналог LIKE из SQL, только с процентом и слева и справа
    //IgnoreCase - без учета регистра
    //все это зарезервированные названия в спринг
    //Можем возвращать не только Collection, но и Stream (batch, close)
    List<Company> findAllByNameContainingIgnoreCase(String name);

    // ТРЕТИЙ СПОСОБ  если выставить nativeQuery в true, то используем нативный SQL
    //по дефолту false, используем HQL(в нем оперируем сущностями)
    //с помощью name можем указать, что используем NamedQuery
//    @Query(name = "Company.findByName") // название NamedQuery
    //fetch нужна, чтобы подтягивать LAZY сущности
    @Query(value = "select c from Company c " +
                   "join fetch c.locales cl " +
                   "where c.name = :name")
    Optional<Company> findByName(String name);
}
