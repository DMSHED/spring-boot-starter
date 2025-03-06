package src.spring.http.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import src.spring.database.entity.Role;
import src.spring.dto.UserCreateEditDto;
import src.spring.dto.UserFilter;
import src.spring.dto.UserReadDto;
import src.spring.service.CompanyService;
import src.spring.service.UserService;

@Slf4j
@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final CompanyService companyService;

    @GetMapping
    public String findAll(Model model,
                          @ModelAttribute("userFilter") UserFilter userFilter) {
//        model.addAttribute("users", userService.findAll());
        model.addAttribute("users", userService.findAll(userFilter));

        return "user/users";
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String findById(@PathVariable("id") Long id,
                           Model model,
                           @CurrentSecurityContext SecurityContext securityContext,
                           @AuthenticationPrincipal UserDetails userDetails) {

        return userService.findById(id)
                .map(user -> {
                    model.addAttribute("user", user);
                    model.addAttribute("roles", Role.values());
                    model.addAttribute("companies", companyService.findAll());
                    return "user/user";
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/registration")
    public String registration(Model model, @ModelAttribute("user") UserCreateEditDto user){
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        model.addAttribute("companies", companyService.findAll());

        return "user/registration";
    }

//    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public String create(@ModelAttribute("user") @Validated UserCreateEditDto user,
                         //нужно чтобы мы могли получить ошибку, которая пробросилась
                         //должен стоять после валидированной формы
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {


        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("user", user);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/users/registration";
        }

        userService.create(user);

        return "redirect:/login";
    }

//    @PutMapping("/{id}") нарушим правила, т.к из формы нельзя put отправлять, потом переделаю
    @PostMapping("/{id}/update")
    public String update(@PathVariable Long id,
                         @ModelAttribute @Validated UserCreateEditDto userDto) {

        return userService.update(id, userDto)
                .map(it -> "redirect:/users/{id}")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    }

//    @DeleteMapping("/{id}") //также из формы нельзя отправить, заколхозим
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        if (!userService.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } else {
            return "redirect:/users";
        }

    }
}
