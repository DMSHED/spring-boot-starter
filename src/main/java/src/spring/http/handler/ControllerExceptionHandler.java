package src.spring.http.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/*
есть уже куча готовых хендлеров ResponseEntityExceptionHandler
 */
@Slf4j
@ControllerAdvice(basePackages = "src.spring.http.controller")
public class ControllerExceptionHandler /*extends ResponseEntityExceptionHandler*/ {

//    @ExceptionHandler(Exception.class)
//    public String handleExceptions(Exception ex) {
//        log.error("Failed to return response", ex);
//        return "error/error500";
//    }

}
