package src.spring.listener.entity;

//import org.junit.jupiter.api.Order;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class EntityListener {

//    @EventListener
//    @Order(10)// сработает после тех у кого меньше 10 и до тех у кого больше
//    public void acceptEntity(EntityEvent entityEvent) {
//        System.out.println("Entity " + entityEvent);
//    }

    //только на AccessType == READ
    //по root.args - обратились к массиву аргументов
    @EventListener(condition = "#root.args[0].accessType.name() == 'READ'")
//    @Order(10)// сработает после тех у кого меньше 10 и до тех у кого больше. отвечает за порядок
    public void acceptEntityRead(EntityEvent entityEvent) {
        System.out.println("Entity " + entityEvent);
    }
}
