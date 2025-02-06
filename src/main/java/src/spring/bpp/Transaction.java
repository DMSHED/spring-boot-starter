package src.spring.bpp;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
// перед вызовом методов класса, будет открываться транзакция, выполяться метод
// а по завершению закрываться
public @interface Transaction {

}
