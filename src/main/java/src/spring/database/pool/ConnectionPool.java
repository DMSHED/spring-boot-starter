package src.spring.database.pool;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

//  ${} - Expression Language, доступ к полям файла properties
//  #{} - Spring Expression Language, внутри есть доступ к бинам, и можно писать java код-->


@Slf4j
@Component("pool1") //нужна, чтобы указать спрингу, что это БинПостПроцессор, раньше в xml указывали
public class ConnectionPool {
    private final String username;
    private final Integer poolSize;
//    private final List<Object> args;
//    private final Map<String, Object> properties;

//    @Autowired //указывает какой конкретно конструктор для инжекта используем, если конструктор один, необязательно
    public ConnectionPool(@Value("${db.username}") String username,
                          @Value("${db.pool.size}") Integer poolSize) {
        this.username = username;
        this.poolSize = poolSize;
//        this.args = args;
//        this.properties = properties;
    }

    //без разницы какой модификатор доступа, они вызываются через Reflection API
    @PostConstruct
    public void init() {
        log.info("Initializing connection pool");
    }

    @PreDestroy
    private void destroy() {
        log.info("Destroying connection pool");
    }


}
