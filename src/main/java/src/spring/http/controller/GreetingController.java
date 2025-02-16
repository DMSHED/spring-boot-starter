package src.spring.http.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import src.spring.database.entity.Role;
import src.spring.dto.UserReadDto;

import java.util.Arrays;
import java.util.List;


@Controller
@RequestMapping("/api/v1")
@SessionAttributes({"user"}) //устанавливаем sessionScope
public class GreetingController {

    @ModelAttribute("roles") //сделали атрибутом, будет вызываться на каждый запрос
    public List<Role> roles() {
        return Arrays.asList(Role.values());
    }

    // spring автоматически нам создаст ModelAndView
    @GetMapping("/hello")
    public ModelAndView hello(ModelAndView modelAndView,
                              HttpServletRequest request,
                              @ModelAttribute UserReadDto userReadDto)
    //если укажем параметры с таким же названием как и в dto
    //то спринг автоматически заинжектит их и создаст модель с такими полями

    {

        modelAndView.setViewName("greeting/hello");
        //в сервлетах делали setAttribute
        //установили атрибут в requestScope
        //когда над контроллером указываем по такому же ключу
        //SessionAttribute, то устанавливается уже в sessionScope
        modelAndView.addObject("user",  userReadDto);

        return modelAndView;
    }

    // spring автоматически нам создаст ModelAndView
    @GetMapping("/hello/{id}")
    public ModelAndView hello2(ModelAndView modelAndView,
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
    public ModelAndView bye(@SessionAttribute("user") UserReadDto user, Model model)
    // Spring неявно вызовет request.getSession().getAttribute("user")
    //нужно сначала вызвать hello чтобы атрибут установился, а в bye мы его получим
    {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("greeting/bye");

        return modelAndView;
    }

}
