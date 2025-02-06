package src.spring.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.*;
import src.spring.database.pool.ConnectionPool;
import src.spring.database.repository.UserRepository;
import src.web.config.WebConfiguration;

@Import(WebConfiguration.class) // можем указать другие конфигурации
@Configuration
//@PropertySource("classpath:application.properties") //чтобы работать с нашими .property
//в спринг бут также не нужна, т.к автоматически будет брать файл с названием application.properties
//@ComponentScan(basePackages = "src.spring") // сканирует классы, у которых есть аннотации Component, Service, Repository, Controller
//в спринг boot не нужна, автоматически сканируется
public class ApplicationConfiguration {

    @Bean(name = "pool2")
//    @Scope(BeanDefinition.SCOPE_PROTOTYPE) по умолчанию singleton
    public ConnectionPool pool2(@Value("${db.username}") String username) {
        return new ConnectionPool(username, 30);
    }
    @Bean
    public ConnectionPool pool3() {
        return new ConnectionPool("test-pool", 50);
    }

//    @Bean
//    @Profile("prod&web") // маркер, "хотим активировать, когда Profile prod и web" можно и над классами конфигами
//    public UserRepository userRepository2(@Qualifier("pool2") ConnectionPool pool2) {
//        return new UserRepository(pool2);
//    }
//    @Bean
//    public UserRepository userRepository3() {
//        return new UserRepository(pool3());
//    }

}
