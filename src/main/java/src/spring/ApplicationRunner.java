package src.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import src.spring.config.ApplicationConfiguration;
import src.spring.database.pool.ConnectionPool;
import src.spring.database.repository.CompanyRepository;
import src.spring.database.repository.UserRepository;
import src.spring.service.CompanyService;
import src.spring.service.UserService;

@SpringBootApplication
@ConfigurationPropertiesScan//будет сканировать все, что помечено @ConfigurationProperties
public class ApplicationRunner {

    public static void main(String[] args) {
        var context = SpringApplication.run(ApplicationRunner.class, args);

        System.out.println(context.getBeanDefinitionCount());
        var companyRepository = context.getBean("companyRepository", CompanyRepository.class);
        System.out.println(companyRepository);
    }




    //тестировали Spring core
//    public static void main(String[] args) {
//        try (var context = new AnnotationConfigApplicationContext()) {
//            context.register(ApplicationConfiguration.class);
//            //также Profile можно установить через контекст(первый вариант в .profiles)
//            context.getEnvironment().setActiveProfiles("prod", "web");
//            context.refresh();
//
//            //если несколько бинов одного и того же класса, то нужно использовать не .class
//            //а уникальный id, это строка
////        System.out.println(context.getBean(ConnectionPool.class));
////            var connectionPool = context.getBean("pool1", ConnectionPool.class);
////        System.out.println(context.getBean("pool1", ConnectionPool.class));
////            var companyRepository = context.getBean("companyRepository", CrudRepository.class);
//
////            System.out.println(companyRepository.findById(1));
//
//            var companyService = context.getBean("companyService", CompanyService.class);
//            System.out.println(companyService.findById(1));
//
//        }
//
//    }
}
