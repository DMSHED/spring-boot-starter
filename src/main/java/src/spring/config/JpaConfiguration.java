package src.spring.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import src.spring.config.condition.JpaCondition;

//для логирования
@Slf4j
@Conditional(JpaCondition.class)
@Configuration
public class JpaConfiguration {

    //заменяем аннотацией @Slf4j
//    private static final Logger log = org.slf4j.LoggerFactory.getLogger(JpaConfiguration.class);

    @PostConstruct
    void init() {
        log.info("JpaConfiguration init");
    }
}
