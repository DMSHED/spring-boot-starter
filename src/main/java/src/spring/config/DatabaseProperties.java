package src.spring.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

//можем смапить .properties или .yml в код
@ConfigurationProperties(prefix = "db")
public record DatabaseProperties(
         String username,
         String password,
         String url,
         String driver,
         String hosts,

         PoolProperties pool,
         List<PoolProperties> pools,
         Map<String, Object> properties) {


    public static record PoolProperties(
            Integer size,
            Integer timeout){

    }
}
