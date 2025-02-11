package src.spring.http.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("/api/v1")
public class GreetingController {

    // spring автоматически нам создаст ModelAndView
    @GetMapping("/hello/{id}")
    public ModelAndView hello(ModelAndView modelAndView,
                              HttpServletRequest request,
                              @PathVariable("id") Integer id,
                              @RequestParam("age") Integer age, //извлекаем обязательный параметр
                              @RequestHeader("accept") String accept, // извлекаем хедер
                              @CookieValue("JSESSIONID") String jsessionId // извлекаем куки
    ) { //с помощью аннотаций в спринге

        //как делал в Servlet
//        String ageParamValue = request.getParameter("age");
//        String acceptServlet = request.getHeader("accept");
//        Cookie[] cookies = request.getCookies();


        modelAndView.setViewName("greeting/hello");

        return modelAndView;
    }

    @GetMapping(path = "/bye")
    public ModelAndView bye(ModelAndView modelAndView) {

        modelAndView.setViewName("greeting/bye");

        return modelAndView;
    }

}
