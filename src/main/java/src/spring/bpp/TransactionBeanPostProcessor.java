package src.spring.bpp;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/*
Метод isAnnotationPresent, проверяет стоит ли над этим классом аннотация в скобочках
 все подмены бинов, к примеру, на Proxy, как ниже, их нужно делать в
 AfterInitialization, чтобы не нарушить жизненный цикл бина
 */
@Component //нужна, чтобы указать спрингу, что это БинПостПроцессор, раньше в xml указывали
public class TransactionBeanPostProcessor implements BeanPostProcessor {

    //сохраняем настоящий класс бина
    private final Map<String, Class<?>> transactionBeans = new HashMap<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean.getClass().isAnnotationPresent(Transaction.class)) {
            transactionBeans.put(beanName, bean.getClass());
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = transactionBeans.get(beanName);
        if (beanClass != null) {
            return Proxy.newProxyInstance(beanClass.getClassLoader(), beanClass.getInterfaces(),
                    (proxy, method, args) -> {
                        System.out.println("Open transaction");//в настоящем приложении будем реально открывать транзакцию
                        try {
                            return method.invoke(bean, args);
                        } catch (Exception e) {
                            System.out.println("Rollback transaction");
                            throw e;
                        } finally {
                            System.out.println("Close transaction");
                        }
                    });
        }
        return bean;
    }

}
